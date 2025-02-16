package my.meetingservice.meeting.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import my.meetingservice.attend.Attend;
import my.meetingservice.attend.repository.AttendRepository;
import my.meetingservice.client.GatheringServiceClient;
import my.meetingservice.client.UserServiceClient;
import my.meetingservice.dto.request.AddMeetingRequest;
import my.meetingservice.dto.request.UpdateMeetingRequest;
import my.meetingservice.dto.response.gathering.GatheringResponse;
import my.meetingservice.dto.response.meeting.*;
import my.meetingservice.dto.response.user.UserResponse;
import my.meetingservice.exception.NoAuthorityException;
import my.meetingservice.exception.NoFoundGatheringException;
import my.meetingservice.exception.NoFoundMeetingException;
import my.meetingservice.exception.NoFoundUserException;
import my.meetingservice.meeting.Meeting;
import my.meetingservice.meeting.repository.MeetingRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static my.meetingservice.util.MeetingConst.*;

@Service
@RequiredArgsConstructor
@Transactional
public class MeetingService {

    private final MeetingRepository meetingRepository;
    private final UserServiceClient userServiceClient;
    private final GatheringServiceClient gatheringServiceClient;
    private final AttendRepository attendRepository;
    @Value("${secret.key}")
    private String secretKey;

    public AddMeetingResponse addMeeting(AddMeetingRequest addMeetingRequest, String token, Long gatheringId) {

        try {
            String username = getUsername(token.replace("Bearer ",""));
            UserResponse userResponse = userServiceClient.fetchUsername(username,token);
            if(!userResponse.getCode().equals("SU")){
                throw  new NoFoundUserException("No User!!");
            }

            GatheringResponse gatheringResponse = gatheringServiceClient.gatheringDetail(gatheringId, token);
            if(!gatheringResponse.getCode().equals("SU")){
                throw new NoFoundGatheringException("No Gathering!!");
            }
            //TODO : gathering 멤버만 meeting 열수있는 로직 추가

            Meeting meeting = Meeting.builder()
                    .title(addMeetingRequest.getTitle())
                    .content(addMeetingRequest.getContent())
                    .createdById(userResponse.getId())
                    .boardDate(LocalDateTime.now())
                    .startDate(addMeetingRequest.getStartDate())
                    .endDate(addMeetingRequest.getEndDate())
                    .gatheringId(gatheringId)
                    .attends(new ArrayList<>())
                    .build();

            meetingRepository.save(meeting);

            Attend attend = Attend.builder()
                    .accepted(true)
                    .attendById(userResponse.getId())
                    .date(LocalDateTime.now())
                    .meeting(meeting)
                    .build();
            attendRepository.save(attend);

            return AddMeetingResponse.builder()
                    .code(successCode)
                    .message(successMessage)
                    .build();
        }catch (NoFoundUserException e){
            return AddMeetingResponse.builder()
                    .code(noFoundUserCode)
                    .message(noFoundUserMessage)
                    .build();
        }catch (NoFoundGatheringException e){
            return AddMeetingResponse.builder()
                    .code(noFoundGatheringCode)
                    .message(noFoundGatheringMessage)
                    .build();
        }catch (Exception e){
            return AddMeetingResponse.builder()
                    .code(failCode)
                    .message(failMessage)
                    .build();
        }
    }

    public DeleteMeetingResponse deleteMeeting(String token, Long meetingId) {
        try {
            String username = getUsername(token.replace("Bearer ",""));
            UserResponse userResponse = userServiceClient.fetchUsername(username,token);
            if(!userResponse.getCode().equals("SU")){
                throw  new NoFoundUserException("No User!!");
            }

            Meeting meeting = meetingRepository.fetchById(meetingId).orElseThrow(() -> new NoFoundMeetingException("No Found Meeting!"));
            boolean authorize = meeting.getCreatedById() == userResponse.getId();
            if(authorize == false){
                throw new NoAuthorityException("No Authority!!");
            }
            meeting.getAttends().stream()
                            .forEach(attend -> attendRepository.delete(attend));
            meetingRepository.delete(meeting);

            return DeleteMeetingResponse.builder()
                    .code(successCode)
                    .message(successMessage)
                    .build();

        }catch (NoFoundMeetingException e){
            return DeleteMeetingResponse.builder()
                    .code(noFoundMeetingCode)
                    .message(noFoundMeetingMessage)
                    .build();
        }catch (NoAuthorityException e){
            return DeleteMeetingResponse.builder()
                    .code(noAuthorityCode)
                    .message(noAuthorityMessage)
                    .build();
        }
        catch (Exception e){
            return DeleteMeetingResponse.builder()
                    .code(failCode)
                    .message(failMessage)
                    .build();
        }

    }

    public UpdateMeetingResponse updateMeeting(UpdateMeetingRequest updateMeetingRequest, String token, Long meetingId) {
        try {
            String username = getUsername(token.replace("Bearer ",""));
            UserResponse userResponse = userServiceClient.fetchUsername(username,token);
            if(!userResponse.getCode().equals("SU")){
                throw  new NoFoundUserException("No User!!");
            }
            Meeting meeting = meetingRepository.fetchById(meetingId).orElseThrow(() -> new NoFoundMeetingException("No Meeting!"));
            boolean authorize = meeting.getCreatedById() == userResponse.getId();
            if(authorize == false){
                throw new NoAuthorityException("No Authority!!");
            }

            meeting.setTitle(updateMeetingRequest.getTitle());
            meeting.setContent(updateMeetingRequest.getContent());
            meeting.setStartDate(updateMeetingRequest.getStartDate());
            meeting.setEndDate(updateMeetingRequest.getEndDate());

            return UpdateMeetingResponse.builder()
                    .code(successCode)
                    .message(successMessage)
                    .build();
        }catch (NoFoundUserException e){
            return UpdateMeetingResponse.builder()
                    .code(noFoundUserCode)
                    .message(noFoundUserMessage)
                    .build();
        }catch (NoFoundMeetingException e){
            return UpdateMeetingResponse.builder()
                    .code(noFoundMeetingCode)
                    .message(noFoundMeetingMessage)
                    .build();
        }catch (NoAuthorityException e){
            return UpdateMeetingResponse.builder()
                    .code(noAuthorityCode)
                    .message(noAuthorityMessage)
                    .build();
        }catch (Exception e) {
            return UpdateMeetingResponse.builder()
                    .code(failCode)
                    .message(failMessage)
                    .build();
        }
    }

    public MeetingResponse meetingDetail(Long meetingId, String token) {
        try {
            String username = getUsername(token.replace("Bearer ",""));
            UserResponse userResponse = userServiceClient.fetchUsername(username,token);
            if(!userResponse.getCode().equals("SU")){
                throw  new NoFoundUserException("No User!!");
            }
            Meeting meeting = meetingRepository.findById(meetingId).orElseThrow(() -> new NoFoundMeetingException("No Meeting!"));

            UserResponse createdByResponse = userServiceClient.fetchUserId(meeting.getCreatedById(),token);
            if(!createdByResponse.getCode().equals("SU")){
                throw new NoFoundUserException("No createdByUser!!");
            }

            return MeetingResponse.builder()
                    .code(successCode)
                    .message(successMessage)
                    .id(meeting.getId())
                    .title(meeting.getTitle())
                    .content(meeting.getContent())
                    .boardDate(meeting.getBoardDate())
                    .startDate(meeting.getStartDate())
                    .endDate(meeting.getEndDate())
                    .createdBy(createdByResponse.getUsername())
                    .attendedBy(new ArrayList<>())
                    .build();

        }catch (NoFoundUserException e){
            return MeetingResponse.builder()
                    .code(noFoundUserCode)
                    .message(noFoundUserMessage)
                    .build();
        }catch (NoFoundMeetingException e){
            return MeetingResponse.builder()
                    .code(noFoundMeetingCode)
                    .message(noFoundMeetingMessage)
                    .build();
        }catch (Exception e){

            return MeetingResponse.builder()
                    .code(failCode)
                    .message(failMessage)
                    .build();

        }
    }

    public MeetingPagingResponse meetings(int pageNum, String token, String title) {
        try {
            String username = getUsername(token.replace("Bearer ",""));
            UserResponse userResponse = userServiceClient.fetchUsername(username,token);
            if(!userResponse.getCode().equals("SU")){
                throw  new NoFoundUserException("No User!!");
            }
            PageRequest pageRequest = PageRequest.of(pageNum - 1, 10, Sort.Direction.ASC,"id");
            Page<Meeting> pageMeeting = meetingRepository.fetchPageMeeting(pageRequest,title);
            Page<MeetingListResponse> pageMeetingListResponse = pageMeeting.map(m ->
                    MeetingListResponse.builder()
                            .id(m.getId())
                            .title(m.getTitle())
                            .createdBy(null)
                            .boardDate(m.getBoardDate())
                            .startDate(m.getStartDate())
                            .endDate(m.getEndDate())
                            .content(m.getContent())
                            .build());

            return MeetingPagingResponse.builder()
                    .code(successCode)
                    .message(successMessage)
                    .meetingListResponses(pageMeetingListResponse)
                    .build();

        }catch (NoFoundUserException e){
            return MeetingPagingResponse.builder()
                    .code(noFoundUserCode)
                    .message(noFoundUserMessage)
                    .build();
        }catch (Exception e){
            return MeetingPagingResponse.builder()
                    .code(failCode)
                    .message(failMessage)
                    .build();

        }
    }

    private String getUsername(String token){
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
}
