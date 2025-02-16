package my.gatheringservice.gathering.service;

import feign.FeignException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.gatheringservice.client.CategoryServiceClient;
import my.gatheringservice.client.ImageServiceClient;
import my.gatheringservice.client.UserServiceClient;
import my.gatheringservice.dto.request.AddGatheringRequest;
import my.gatheringservice.dto.request.UpdateGatheringRequest;
import my.gatheringservice.dto.response.*;
import my.gatheringservice.dto.response.category.CategoryListResponse;
import my.gatheringservice.dto.response.category.CategoryResponse;
import my.gatheringservice.dto.response.gathering.*;
import my.gatheringservice.dto.response.image.ImageListResponse;
import my.gatheringservice.dto.response.image.ImageResponse;
import my.gatheringservice.dto.response.user.UserListResponse;
import my.gatheringservice.dto.response.user.UserResponse;
import my.gatheringservice.exception.NoAuthorityException;
import my.gatheringservice.exception.NotFoundGatheringException;
import my.gatheringservice.gathering.Gathering;
import my.gatheringservice.gathering.GatheringCount;
import my.gatheringservice.gathering.repository.GatheringRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.Key;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import static my.gatheringservice.dto.response.gathering.GatheringResponse.toGatheringResponse;
import static my.gatheringservice.util.GatheringConst.*;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class GatheringService {

    private final UserServiceClient userServiceClient;
    private final ImageServiceClient imageServiceClient;
    private final CategoryServiceClient categoryServiceClient;
    private final GatheringRepository gatheringRepository;
    private final GatheringCountService gatheringCountService;
    @Value("${secret.key}")
    private String secretKey;



    public AddGatheringResponse addGathering(AddGatheringRequest addGatheringRequest, MultipartFile file, String token) {

        try {
            String username = getUsername(token.replace("Bearer ",""));
            UserResponse userResponse = userServiceClient.fetchUsername(username,token);
            ImageResponse imageResponse = imageServiceClient.upload(file,token);
            CategoryResponse categoryResponse = categoryServiceClient.fetchName(addGatheringRequest.getCategory(),token);

            Gathering gathering = AddGatheringRequest.toGathering(addGatheringRequest,userResponse,imageResponse,categoryResponse);
            gatheringCountService.makeCount(gathering);
            gatheringRepository.save(gathering);


            return AddGatheringResponse.builder()
                    .code(successCode)
                    .message(successMessage)
                    .build();


        }catch (FeignException e){
            log.error("exception",e);
            return AddGatheringResponse.builder()
                    .code(feignFailCode)
                    .code(feignFailMessage)
                    .build();
        }catch (Exception e){
            log.error("exception",e);
            return AddGatheringResponse.builder()
                    .code(failCode)
                    .code(failMessage)
                    .build();
        }


    }

    public UpdateGatheringResponse updateGathering(UpdateGatheringRequest updateGatheringRequest, MultipartFile file, String token, Long gatheringId) {

        try {

            ImageResponse imageResponse = null;
            String username = getUsername(token.replace("Bearer ",""));
            UserResponse userResponse = userServiceClient.fetchUsername(username,token);
            CategoryResponse categoryResponse = categoryServiceClient.fetchName(updateGatheringRequest.getCategory(),token);
            Gathering gathering = gatheringRepository.findById(gatheringId).orElseThrow(() -> new NotFoundGatheringException("Not Found Gathering!!"));

            if(userResponse.getId() != gathering.getCreateById()){
                throw new NoAuthorityException("this user didnt make this gathering");
            }

            if(!file.isEmpty()){

                imageResponse = imageServiceClient.upload(file,token);

            }


            updateGatheringRequest.updateGathering(gathering,updateGatheringRequest,imageResponse,categoryResponse);
            return UpdateGatheringResponse.builder()
                    .code(successCode)
                    .message(successMessage)
                    .build();

        }catch (NoAuthorityException e){
            log.error("exception",e);
            return UpdateGatheringResponse.builder()
                    .code(noAuthorityCode)
                    .message(noAuthorityMessage)
                    .build();

        }catch (NotFoundGatheringException e){
            log.error("exception",e);
            return UpdateGatheringResponse.builder()
                    .code(notFoundGatheringCode)
                    .message(notFoundGatheringMessage)
                    .build();
        }catch (FeignException e){
            log.error("exception",e);
            return UpdateGatheringResponse.builder()
                    .code(feignFailCode)
                    .code(feignFailMessage)
                    .build();
        }
        catch (Exception e){
            log.error("exception",e);
            return UpdateGatheringResponse.builder()
                    .code(failCode)
                    .message(failMessage)
                    .build();
        }


    }

    public GatheringResponse gatheringDetail(Long gatheringId, String token) {
        try {
            Gathering gathering = gatheringRepository.fetchGathering(gatheringId).orElseThrow(() -> new NotFoundGatheringException("Not Found Gathering!!"));
            GatheringCount gatheringCount = gathering.getGatheringCount();
            UserResponse userResponse = userServiceClient.fetchUserId(gathering.getCreateById(),token);
            ImageResponse imageResponse = imageServiceClient.fetch(gathering.getImageId(),token);
            CategoryResponse categoryResponse = categoryServiceClient.fetchId(gathering.getCategoryId(),token);



            return toGatheringResponse(gathering,gatheringCount,categoryResponse,imageResponse,userResponse);
        }catch (FeignException e){
            log.error("exception",e);
            return GatheringResponse.builder()
                    .code(feignFailCode)
                    .code(feignFailMessage)
                    .build();
        }catch (NotFoundGatheringException e){
            log.error("exception",e);
            return GatheringResponse.builder()
                    .code(notFoundGatheringCode)
                    .message(notFoundGatheringMessage)
                    .build();
        }catch (IOException e){
            log.error("exception",e);
            return GatheringResponse.builder()
                    .code(noEncodeCode)
                    .message(noEncodeMessage)
                    .build();
        }
        catch (Exception e){
            log.error("exception",e);
            return GatheringResponse.builder()
                    .code(failCode)
                    .message(failMessage)
                    .build();
        }
    }

    public GatheringPagingResponse gatherings(int pageNum, String token, String title) {

        try {

            PageRequest pageRequest = PageRequest.of(pageNum - 1, 10, Sort.Direction.ASC,"id");
            Page<Gathering> gatheringPage = gatheringRepository.fetchPage(pageRequest,title);


            List<Long> categoryIdList = gatheringPage.getContent().stream().map(Gathering::getCategoryId)
                    .collect(Collectors.toList());

            List<Long> createdIdList = gatheringPage.getContent().stream().map(Gathering::getCreateById)
                    .collect(Collectors.toList());

            List<Long> imageIdList = gatheringPage.getContent().stream().map(Gathering::getImageId)
                    .collect(Collectors.toList());

            CategoryListResponse categoryListResponse = categoryServiceClient.fetchList(categoryIdList,token);
            UserListResponse userListResponse = userServiceClient.fetchList(createdIdList,token);
            ImageListResponse imageListResponse = imageServiceClient.fetchList(imageIdList, token);


            Page<GatheringPagingElement> page = gatheringPage.map(g ->
                 GatheringPagingElement.builder()
                        .id(g.getId())
                        .title(g.getTitle())
                        .content(g.getContent())
                        .registerDate(g.getRegisterDate())
                        .category(matchCategory(g,categoryListResponse))
                        .createdBy(matchCreatedBy(g,userListResponse))
                        .image(encodeFileToBase64(matchImageUrl(g,imageListResponse)))
                        .count(g.getGatheringCount().getCount())
                        .build()
            );

            return GatheringPagingResponse.builder()
                    .code(successCode)
                    .message(successMessage)
                    .page(page)
                    .build();



        }catch (FeignException e){
            return GatheringPagingResponse.builder()
                    .code(feignFailCode)
                    .message(feignFailMessage)
                    .build();
        }catch (Exception e){
            return GatheringPagingResponse.builder()
                    .code(failCode)
                    .message(failMessage)
                    .build();
        }


    }




    private String encodeFileToBase64(String filePath){
        try {
            File file = new File(filePath);
            byte[] fileBytes  = Files.readAllBytes(file.toPath());
            return Base64.getEncoder().encodeToString(fileBytes);
        }catch (IOException e){
            return null;
        }

    }

    private String matchCategory(Gathering gathering, CategoryListResponse categoryListResponse){
        return categoryListResponse.getCategoryLIstElements().stream()
                .filter(c -> c.getId().equals(gathering.getCategoryId()))
                .map(c-> c.getName())
                .findFirst()
                .orElse(null);
    }

    private String matchCreatedBy(Gathering gathering, UserListResponse userListResponse){
        return userListResponse.getUsers().stream()
                .filter(u -> u.getId().equals(gathering.getCreateById()))
                .map(u->u.getUsername())
                .findFirst()
                .orElse(null);
    }

    private String matchImageUrl(Gathering gathering, ImageListResponse imageListResponse){
        return imageListResponse.getImageListElements().stream()
                .filter(i -> i.getId().equals(gathering.getImageId()))
                .map(i->i.getUrl())
                .findFirst()
                .orElse(null);
    }

    private String getUsername(String token) {
        Claims claims = null;
        String subject =null;

        Key key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)) ;


        try{

            claims = Jwts.parserBuilder().setSigningKey(key)
                    .build()
                    .parseClaimsJws(token).getBody();

            subject = claims.getSubject();


        } catch(Exception exception){
            exception.printStackTrace();
            return null;

        }

        return subject;
    }

    public GatheringPagingResponse gatheringsLike(int pageNum, String token) {
        return null;
    }
}
