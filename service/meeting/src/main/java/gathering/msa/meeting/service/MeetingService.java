package gathering.msa.meeting.service;

import dto.request.meeting.AddMeetingRequest;
import dto.request.meeting.UpdateMeetingRequest;
import dto.response.gathering.GatheringPagingResponse;
import dto.response.gathering.GatheringResponse;
import dto.response.gathering.GatheringsResponse;
import dto.response.image.SaveImageResponse;
import dto.response.meeting.*;
import dto.response.user.UserResponse;
import event.EventType;
import event.payload.MeetingCreatedEventPayload;
import event.payload.MeetingDeleteEventPayload;
import event.payload.MeetingUpdateEventPayload;
import exception.image.ImageUploadFailException;
import exception.meeting.MeetingIsNotEmptyException;
import exception.meeting.NotAuthorizeException;
import exception.meeting.NotFoundMeetingExeption;
import exception.user.NotFoundUserException;
import gathering.msa.meeting.client.GatheringServiceClient;
import gathering.msa.meeting.client.ImageServiceClient;
import gathering.msa.meeting.client.UserServiceClient;
import gathering.msa.meeting.entity.Attend;
import gathering.msa.meeting.entity.Meeting;
import gathering.msa.meeting.entity.MeetingCount;
import gathering.msa.meeting.repository.AttendRepository;
import gathering.msa.meeting.repository.MeetingCountRepository;
import gathering.msa.meeting.repository.MeetingRepository;
import gathering.msa.outbox.OutboxEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import snowflake.Snowflake;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static util.ConstClass.*;


@Service
@Transactional
@RequiredArgsConstructor
public class MeetingService {

    private final MeetingRepository meetingRepository;
    private final AttendRepository attendRepository;
    private final MeetingCountRepository meetingCountRepository;
    private final UserServiceClient userServiceClient;
    private final ImageServiceClient imageServiceClient;
    private final GatheringServiceClient gatheringServiceClient;
    private final Snowflake snowflake = new Snowflake();
    private final OutboxEventPublisher outboxEventPublisher;
    @Value("${server.url}")
    private String url;
    public AddMeetingResponse addMeeting(AddMeetingRequest addMeetingRequest, String username, Long gatheringId, MultipartFile file) throws IOException {

        UserResponse userResponse = userServiceClient.fetchUserByUsername(username);
        if(!userResponse.getCode().equals(SUCCESS_CODE)) throw new NotFoundUserException("no exist User!!");
        GatheringResponse gatheringResponse = gatheringServiceClient.fetchFeignGathering(gatheringId);
        if(!gatheringResponse.getCode().equals(SUCCESS_CODE)) throw new NotFoundUserException("no exist User!!");
        SaveImageResponse saveImageResponse = imageServiceClient.saveImage(List.of(file),null);
        if(!saveImageResponse.getCode().equals(SUCCESS_CODE)) throw new ImageUploadFailException("image upload fail");
        Meeting meeting = Meeting.of(snowflake,addMeetingRequest,saveImageResponse,userResponse,gatheringResponse);
        Attend attend = Attend.of(snowflake,false,meeting,userResponse,LocalDateTime.now());
        MeetingCount meetingCount = MeetingCount.of(snowflake,gatheringId,meeting);
        meetingRepository.save(meeting);
        attendRepository.save(attend);
        meetingCountRepository.save(meetingCount);
        outboxEventPublisher.publish(EventType.MEETING_CREATED,
                MeetingCreatedEventPayload.builder()
                        .id(meeting.getId())
                        .title(meeting.getTitle())
                        .boardDate(meeting.getBoardDate())
                        .startDate(meeting.getStartDate())
                        .endDate(meeting.getEndDate())
                        .content(meeting.getContent())
                        .userId(meeting.getUserId())
                        .gatheringId(meeting.getGatheringId())
                        .imageId(meeting.getImageId())
                        .build(),
                gatheringId
                );
        return AddMeetingResponse.of(SUCCESS_CODE, SUCCESS_MESSAGE);
    }

    public DeleteMeetingResponse deleteMeeting(String username, Long meetingId,Long gatheringId) {

        UserResponse userResponse = userServiceClient.fetchUserByUsername(username);
        if(!userResponse.getCode().equals(SUCCESS_CODE)) throw new NotFoundUserException("no exist User!!");
        Meeting meeting = meetingRepository.findById(meetingId).orElseThrow(()->new NotFoundMeetingExeption("no exist Meeting!!"));
        MeetingCount meetingCount = meetingCountRepository.findByMeetingId(meetingId).orElseThrow();
        boolean authorize = meeting.getUserId() == userResponse.getId();
        if(authorize == false){
            throw new NotAuthorizeException("no authority!");
        }
        if(meetingCount.getCount() > 1){
            throw new MeetingIsNotEmptyException("meeting is not empty!!");
        }
        Attend attend = attendRepository.findByUserIdAndMeetingIdAndTrue(userResponse.getId(), meetingId);
        attendRepository.delete(attend);
        meetingRepository.delete(meeting);
        meetingCountRepository.delete(meetingCount);
        outboxEventPublisher.publish(EventType.MEETING_DELETED,
                MeetingDeleteEventPayload.builder()
                        .id(meeting.getId())
                        .title(meeting.getTitle())
                        .boardDate(meeting.getBoardDate())
                        .startDate(meeting.getStartDate())
                        .endDate(meeting.getEndDate())
                        .content(meeting.getContent())
                        .userId(meeting.getUserId())
                        .gatheringId(meeting.getGatheringId())
                        .imageId(meeting.getImageId())
                        .build(),
                gatheringId
        );
        return DeleteMeetingResponse.builder()
                .code(SUCCESS_CODE)
                .message(SUCCESS_MESSAGE)
                .build();
    }

    public UpdateMeetingResponse updateMeeting(UpdateMeetingRequest updateMeetingRequest, String username, Long meetingId, MultipartFile file,Long gatheringId) throws IOException {

        UserResponse userResponse = userServiceClient.fetchUserByUsername(username);
        if(!userResponse.getCode().equals(SUCCESS_CODE)) throw new NotFoundUserException("no exist User!!");
        Meeting meeting = meetingRepository.findById(meetingId).orElseThrow(()->new NotFoundMeetingExeption("no exist Meeting!!"));
            boolean authorize = meeting.getUserId() == userResponse.getId();
            if(authorize == false){
                throw new NotAuthorizeException("no authority!");
            }
        SaveImageResponse saveImageResponse = imageServiceClient.saveImage(List.of(file),null);
        if(!saveImageResponse.getCode().equals(SUCCESS_CODE)) throw new ImageUploadFailException("image upload fail");
        meeting.setTitle(updateMeetingRequest.getTitle());
        meeting.setContent(updateMeetingRequest.getContent());
        meeting.setStartDate(updateMeetingRequest.getStartDate());
        meeting.setEndDate(updateMeetingRequest.getEndDate());
        meeting.setBoardDate(LocalDateTime.now());
        meeting.setImageId(saveImageResponse.getIds().getFirst());
        outboxEventPublisher.publish(EventType.MEETING_UPDATED,
                MeetingUpdateEventPayload.builder()
                        .id(meeting.getId())
                        .title(meeting.getTitle())
                        .boardDate(meeting.getBoardDate())
                        .startDate(meeting.getStartDate())
                        .endDate(meeting.getEndDate())
                        .content(meeting.getContent())
                        .userId(meeting.getUserId())
                        .gatheringId(meeting.getGatheringId())
                        .imageId(meeting.getImageId())
                        .build(),
                gatheringId
        );
        return UpdateMeetingResponse.builder()
                .code(SUCCESS_CODE)
                .message(SUCCESS_MESSAGE)
                .build();
    }
    public MeetingResponse meetingDetail(Long meetingId, String username,Long gatheringId) {

        UserResponse userResponse = userServiceClient.fetchUserByUsername(username);
        if(!userResponse.getCode().equals(SUCCESS_CODE)) throw new NotFoundUserException("no exist User!!");
        List<MeetingDetailQuery> meetingDetailQueries = meetingRepository.meetingDetail(meetingId);
        if(meetingDetailQueries.size() == 0) throw new NotFoundMeetingExeption("no exist Meeting!!");
        return toMeetingResponse(meetingDetailQueries);

    }

    public MeetingsResponse meetings(int pageNum, int pageSize, String username, String title,Long gatheringId) {

        UserResponse userResponse = userServiceClient.fetchUserByUsername(username);
        if(!userResponse.getCode().equals(SUCCESS_CODE)) throw new NotFoundUserException("no exist User!!");
        PageRequest pageRequest = PageRequest.of(pageNum - 1, pageSize, Sort.Direction.ASC,"id");
        Page<MeetingsQuery> page = meetingRepository.meetings(pageRequest,title);
        return toMeetingsResponse(page);
    }

    private MeetingsResponse toMeetingsResponse(Page<MeetingsQuery> meetingsQueryPage) {

        boolean hasNext = meetingsQueryPage.hasNext();
        List<Long> createdByIds = meetingsQueryPage.stream()
                .map(m -> m.getCreatedById())
                .toList();
        List<Long> imageIds = meetingsQueryPage.stream()
                .map(m -> m.getImageId())
                .toList();

        List<String> urls = imageServiceClient.url(imageIds).getUrls();
        List<String> usernames = userServiceClient.fetchUserByIds(createdByIds).getElements()
                .stream()
                .map(u -> u.getUsername())
                .toList();

        List<MeetingsQuery> meetingsQueries = meetingsQueryPage.getContent();
        List<MeetingsPage> result = IntStream.range(0, meetingsQueries.size())
                .mapToObj(i -> MeetingsPage.builder()
                        .id(meetingsQueries.get(i).getId())
                        .title(meetingsQueries.get(i).getTitle())
                        .createdBy(usernames.get(i))
                        .boardDate(meetingsQueries.get(i).getBoardDate())
                        .startDate(meetingsQueries.get(i).getStartDate())
                        .endDate(meetingsQueries.get(i).getEndDate())
                        .content(meetingsQueries.get(i).getContent())
                        .count(meetingsQueries.get(i).getCount())
                        .url(getUrl(urls.get(i)))
                        .build())
                .collect(Collectors.toList());
        return MeetingsResponse.builder()
                .hasNext(hasNext)
                .code(SUCCESS_CODE)
                .message(SUCCESS_MESSAGE)
                .elements(result)
                .build();

    }

    private MeetingResponse toMeetingResponse(List<MeetingDetailQuery> meetingDetailQueries){

        List<Long> createdById = List.of(meetingDetailQueries.getFirst().getImageId());
        List<Long> imageId = List.of(meetingDetailQueries.getFirst().getCreatedById());
        List<Long> attendByIds = meetingDetailQueries.stream()
                .map(m->m.getAttendedById())
                .toList();
        List<String> attendedBy = new ArrayList<>();
        String url = imageServiceClient.url(imageId).getUrls().getFirst();
        String createdBy = userServiceClient.fetchUserByIds(createdById).getElements().getFirst().getUsername();
        List<String> attendBy = userServiceClient.fetchUserByIds(attendByIds).getElements().stream()
                .map(u->u.getUsername())
                .toList();

        return MeetingResponse.builder()
                .code(SUCCESS_CODE)
                .message(SUCCESS_MESSAGE)
                .id(meetingDetailQueries.getFirst().getId())
                .title(meetingDetailQueries.getFirst().getTitle())
                .content(meetingDetailQueries.getFirst().getContent())
                .boardDate(meetingDetailQueries.getFirst().getBoardDate())
                .startDate(meetingDetailQueries.getFirst().getStartDate())
                .endDate(meetingDetailQueries.getFirst().getEndDate())
                .createdBy(createdBy)
                .attendedBy(attendBy)
                .url(url)
                .build();
    }

    private String getUrl(String fileUrl){
        return url+fileUrl;
    }
}
