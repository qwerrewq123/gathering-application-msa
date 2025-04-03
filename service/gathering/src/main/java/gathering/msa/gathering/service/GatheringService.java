package gathering.msa.gathering.service;

import dto.request.gathering.AddGatheringRequest;
import dto.request.gathering.UpdateGatheringRequest;
import dto.response.gathering.*;
import dto.response.image.ImageUrlResponse;
import dto.response.image.SaveImageResponse;
import dto.response.user.UserResponse;
import dto.response.user.UserResponses;
import dto.response.user.UserResponsesElement;
import event.Event;
import event.EventType;
import event.payload.GatheringCreatedEventPayload;
import event.payload.GatheringUpdateEventPayload;
import event.payload.GatheringViewEventPayload;
import exception.category.NotFoundCategoryException;
import exception.gathering.NotFoundGatheringException;
import exception.image.ImageUploadFailException;
import exception.meeting.NotAuthorizeException;
import exception.user.NotFoundUserException;
import gathering.msa.gathering.client.ImageServiceClient;
import gathering.msa.gathering.client.UserServiceClient;
import gathering.msa.gathering.entity.Category;
import gathering.msa.gathering.entity.Enrollment;
import gathering.msa.gathering.entity.Gathering;
import gathering.msa.gathering.entity.GatheringCount;
import gathering.msa.gathering.repository.CategoryRepository;
import gathering.msa.gathering.repository.EnrollmentRepository;
import gathering.msa.gathering.repository.GatheringCountRepository;
import gathering.msa.gathering.repository.GatheringRepository;
import gathering.msa.outbox.OutboxEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import snowflake.Snowflake;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static util.ConstClass.*;


@Service
@Transactional
@RequiredArgsConstructor
public class GatheringService {
    private final GatheringViewService gatheringViewService;
    private final GatheringRepository gatheringRepository;
    private final CategoryRepository categoryRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final GatheringCountRepository gatheringCountRepository;
    private final ImageServiceClient imageServiceClient;
    private final UserServiceClient userServiceClient;
    private final Snowflake snowflake = new Snowflake();
    private final OutboxEventPublisher outboxEventPublisher;

    @Value("${server.url}")
    private String url;

    public AddGatheringResponse addGathering(AddGatheringRequest addGatheringRequest, MultipartFile file, String username) throws IOException {

        UserResponse userResponse = userServiceClient.fetchUserByUsername(username);
        if(!userResponse.getCode().equals(SUCCESS_CODE)) throw new NotFoundUserException("no exist User!!");
        Category category = categoryRepository.findByName(addGatheringRequest.getCategory()).orElseThrow(
                ()-> new NotFoundCategoryException("no exist Category!!"));
        SaveImageResponse saveImageResponse = imageServiceClient.saveImage(List.of(file),null);
        if(!saveImageResponse.getCode().equals(SUCCESS_CODE)) throw new ImageUploadFailException("image upload fail");
        Gathering gathering = Gathering.of(snowflake,addGatheringRequest,userResponse,category,saveImageResponse);
        gatheringRepository.save(gathering);
        enrollmentRepository.save(Enrollment.of(snowflake,true,gathering,userResponse, LocalDateTime.now()));
        gatheringCountRepository.save(GatheringCount.of(snowflake,gathering,1));

        outboxEventPublisher.publish(EventType.GATHERING_CREATED,
                GatheringCreatedEventPayload.builder()
                        .id(gathering.getId())
                        .title(gathering.getTitle())
                        .content(gathering.getContent())
                        .registerDate(gathering.getRegisterDate())
                        .categoryId(category.getId())
                        .userId(gathering.getUserId())
                        .imageId(gathering.getImageId())
                        .build(),
                gathering.getId()
                );
        return AddGatheringResponse.of(SUCCESS_CODE,SUCCESS_MESSAGE);

    }

    public UpdateGatheringResponse updateGathering(UpdateGatheringRequest updateGatheringRequest, MultipartFile file, String username, Long gatheringId) throws IOException {

        UserResponse userResponse = userServiceClient.fetchUserByUsername(username);
        if(!userResponse.getCode().equals(SUCCESS_CODE)) throw new NotFoundUserException("no exist User!!");
        Category category = categoryRepository.findByName(updateGatheringRequest.getCategory()).orElseThrow(
                ()-> new NotFoundCategoryException("no exist Category!!"));
        SaveImageResponse saveImageResponse = imageServiceClient.saveImage(List.of(file),null);
        if(!saveImageResponse.getCode().equals(SUCCESS_CODE)) throw new ImageUploadFailException("image upload fail");
        Gathering gathering = gatheringRepository.findById(gatheringId).orElseThrow(()->new NotFoundGatheringException("no exist Gathering!!"));
        boolean authorize = gathering.getUserId() == userResponse.getId();
        if(!authorize) throw new NotAuthorizeException("no authorize!!");
        gathering.changeGathering(updateGatheringRequest,saveImageResponse,category);
        outboxEventPublisher.publish(EventType.GATHERING_UPDATED,
                GatheringUpdateEventPayload.builder()
                        .id(gathering.getId())
                        .title(gathering.getTitle())
                        .content(gathering.getContent())
                        .registerDate(gathering.getRegisterDate())
                        .categoryId(category.getId())
                        .userId(gathering.getUserId())
                        .imageId(gathering.getImageId())
                        .build(),
                gathering.getId()
        );
        return UpdateGatheringResponse.of(SUCCESS_CODE,SUCCESS_MESSAGE);
    }
    public GatheringResponse fetchFeignGathering(Long gatheringId) {
        gatheringRepository.findById(gatheringId).orElseThrow(() -> new NotFoundGatheringException("no exist Gathering!!"));
        GatheringResponse gatheringResponse = new GatheringResponse();
        gatheringResponse.setCode(SUCCESS_CODE);
        gatheringResponse.setMessage(SUCCESS_MESSAGE);
        gatheringResponse.setId(gatheringId);
        return gatheringResponse;
    }


    public GatheringResponse gatheringDetail(Long gatheringId, String username) throws IOException {
        UserResponse userResponse = userServiceClient.fetchUserByUsername(username);
        if(!userResponse.getCode().equals(SUCCESS_CODE)) throw new NotFoundUserException("no exist User!!");
        List<GatheringDetailQuery> gatheringDetailQueries = gatheringRepository.gatheringDetail(gatheringId);
        if(gatheringDetailQueries.isEmpty()) throw new NotFoundGatheringException("no exist Gathering!!!");
        gatheringViewService.fetchCount(gatheringId);
        Category category = categoryRepository.findByName(gatheringDetailQueries.get(0).getCategory()).orElseThrow(() -> new NotFoundCategoryException("no exist Category!!"));
        outboxEventPublisher.publish(EventType.GATHERING_VIEW,
                GatheringViewEventPayload.builder()
                        .id(gatheringDetailQueries.getFirst().getId())
                        .title(gatheringDetailQueries.getFirst().getTitle())
                        .content(gatheringDetailQueries.getFirst().getContent())
                        .registerDate(gatheringDetailQueries.getFirst().getRegisterDate())
                        .categoryId(category.getId())
                        .userId(gatheringDetailQueries.getFirst().getCreatedById())
                        .imageId(gatheringDetailQueries.getFirst().getImageId())
                        .build(),
                gatheringDetailQueries.getFirst().getId()
        );
        return getGatheringResponse(gatheringDetailQueries);
    }
    public GatheringPagingResponse gatheringCategory(String category, Integer pageNum, Integer pageSize, String username) {
        UserResponse userResponse = userServiceClient.fetchUserByUsername(username);
        if(!userResponse.getCode().equals(SUCCESS_CODE)) throw new NotFoundUserException("no exist User!!");
        PageRequest pageRequest = PageRequest.of(pageNum - 1, pageSize, Sort.Direction.ASC,"id");
        Page<GatheringsQuery> page = gatheringRepository.gatheringsCategory(pageRequest,category);
        return toGatheringsResponsePage(page);
    }
    public TotalGatheringsResponse gatherings(String username, String title) {
        UserResponse userResponse = userServiceClient.fetchUserByUsername(username);
        if(!userResponse.getCode().equals(SUCCESS_CODE)) throw new NotFoundUserException("no exist User!!");
        List<EntireGatheringsQuery> entireGatheringsQueries = gatheringRepository.gatherings(title);
        List<GatheringsQuery> gatherings = toGatheringQueriesList(entireGatheringsQueries);
        List<TotalGatheringsElement> totalGatheringsElements = toGatheringsResponseList(gatherings);
        Map<String, CategoryTotalGatherings> map = categorizeByCategory(totalGatheringsElements);
        return createTotalGatherings(map);

    }

    private GatheringResponse getGatheringResponse(List<GatheringDetailQuery> gatheringDetailQueries) throws IOException {

        List<Long> participatedIds = gatheringDetailQueries
                .stream()
                .map(q-> q.getParticipatedById())
                .toList();
        UserResponses userResponses =  userServiceClient.fetchUserByIds(participatedIds);
        List<String> usernames = userResponses.getElements()
                .stream()
                .map(u -> u.getUsername())
                .toList();
        GatheringResponse gatheringResponse = GatheringResponse.builder()
                .code("SU")
                .message("Success")
                .title(gatheringDetailQueries.getFirst().getTitle())
                .content(gatheringDetailQueries.getFirst().getContent())
                .registerDate(gatheringDetailQueries.getFirst().getRegisterDate())
                .category(gatheringDetailQueries.getFirst().getCategory())
                .createdBy(userServiceClient.fetchUserByIds(List.of(gatheringDetailQueries.getFirst().getCreatedById())).getElements().getFirst().getUsername())
                .image(imageServiceClient.url(List.of(gatheringDetailQueries.getFirst().getImageId())).getUrls().getFirst())
                .count(gatheringDetailQueries.getFirst().getCount())
                .participatedBy(usernames)
                .build();

        return gatheringResponse;
    }
    private List<GatheringsQuery> toGatheringQueriesList(List<EntireGatheringsQuery> entireGatheringsQueries) {
        return entireGatheringsQueries.stream()
                .map(query -> GatheringsQuery.builder()
                        .id(query.getId())
                        .title(query.getTitle())
                        .content(query.getContent())
                        .registerDate(query.getRegisterDate())
                        .category(query.getCategory())
                        .createdById(query.getCreatedById())
                        .imageId(query.getImageId())
                        .count(query.getCount())
                        .build())
                .collect(Collectors.toList());
    }
//
    private List<TotalGatheringsElement> toGatheringsResponseList(List<GatheringsQuery> gatheringsQueryList) {
        List<Long> imageIds = gatheringsQueryList.stream()
                .map(g -> g.getImageId())
                .toList();
        List<Long> createByIds = gatheringsQueryList.stream()
                .map(g -> g.getCreatedById())
                .toList();
        List<String> usernames = userServiceClient.fetchUserByIds(createByIds).getElements().stream()
                        .map(u -> u.getUsername())
                                .toList();
        List<String> urls = imageServiceClient.url(imageIds).getUrls();

        return IntStream.range(0, gatheringsQueryList.size())
                .mapToObj(i -> TotalGatheringsElement.builder()
                        .id(gatheringsQueryList.get(i).getId())
                        .title(gatheringsQueryList.get(i).getTitle())
                        .content(gatheringsQueryList.get(i).getContent())
                        .registerDate(gatheringsQueryList.get(i).getRegisterDate())
                        .category(gatheringsQueryList.get(i).getCategory())
                        .createdBy(usernames.get(i))
                        .url(getUrl(urls.get(i)))
                        .count(gatheringsQueryList.get(i).getCount())
                        .build())
                .collect(Collectors.toList());

    }

    public Map<String, CategoryTotalGatherings> categorizeByCategory(List<TotalGatheringsElement> totalGatheringsElements) {
        return totalGatheringsElements.stream()
                .collect(Collectors.groupingBy(
                        TotalGatheringsElement::getCategory,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                this::processCategoryElements
                        )
                ));
    }

    private CategoryTotalGatherings processCategoryElements(List<TotalGatheringsElement> elements) {
        boolean hasNext = elements.size() >= 9;
        if (hasNext) {
            elements = elements.subList(0, 8);
        }

        return CategoryTotalGatherings.builder()
                .totalGatherings(elements)
                .hasNext(hasNext)
                .build();
    }
    private TotalGatheringsResponse createTotalGatherings(Map<String, CategoryTotalGatherings> categoryMap) {
        return TotalGatheringsResponse.builder()
                .code(SUCCESS_CODE)
                .message(SUCCESS_MESSAGE)
                .map(categoryMap)
                .build();
    }


    private GatheringPagingResponse toGatheringsResponsePage(Page<GatheringsQuery> page) {
        boolean hasNext = page.hasNext();
        List<Long> createdByIds = page.getContent().stream().map(GatheringsQuery::getCreatedById).toList();
        UserResponses userResponses = userServiceClient.fetchUserByIds(createdByIds);
        List<Long> urlIds = page.getContent().stream().map(GatheringsQuery::getImageId).toList();
        ImageUrlResponse imageUrlResponse = imageServiceClient.url(urlIds);
        List<String> usernames = userResponses.getElements().stream()
                        .map(
                                element -> element.getUsername()
                        ).toList();
        List<String> urls = imageUrlResponse.getUrls();
        List<GatheringsQuery> gatheringsQueries = page.getContent();
        List<GatheringsResponse> gatheringsResponses = IntStream.range(0, gatheringsQueries.size())
                .mapToObj(i -> GatheringsResponse.builder()
                        .id(gatheringsQueries.get(i).getId())
                        .title(gatheringsQueries.get(i).getTitle())
                        .content(gatheringsQueries.get(i).getContent())
                        .registerDate(gatheringsQueries.get(i).getRegisterDate())
                        .category(gatheringsQueries.get(i).getCategory())
                        .createdBy(usernames.get(i))
                        .url(getUrl(urls.get(i)))
                        .count(gatheringsQueries.get(i).getCount())
                        .build())
                .collect(Collectors.toList());
        return GatheringPagingResponse.builder()
                .hasNext(hasNext)
                .code(SUCCESS_CODE)
                .message(SUCCESS_MESSAGE)
                .elements(gatheringsResponses)
                .build();

    }
    private String getUrl(String fileUrl){
        return url+fileUrl;
    }

    public GatheringResponses fetchFeignGatherings(List<Long> gatheringIds) {
        Page<GatheringResponseQuery> page = gatheringRepository.findByIds(gatheringIds);
        List<GatheringResponseElement> content = page.map(query -> GatheringResponseElement.of(query)).getContent();
        return GatheringResponses.builder()
                .code(SUCCESS_CODE)
                .message(SUCCESS_MESSAGE)
                .content(content)
                .build();

    }
}
