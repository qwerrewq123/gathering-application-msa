package my.meetingservice.attend.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import my.meetingservice.attend.Attend;
import my.meetingservice.attend.repository.AttendRepository;
import my.meetingservice.client.UserServiceClient;
import my.meetingservice.dto.response.AddAttendResponse;
import my.meetingservice.dto.response.DisAttendResponse;
import my.meetingservice.dto.response.PermitAttendResponse;
import my.meetingservice.dto.response.user.UserResponse;
import my.meetingservice.exception.*;
import my.meetingservice.meeting.Meeting;
import my.meetingservice.meeting.repository.MeetingRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDateTime;

import static my.meetingservice.util.MeetingConst.*;

@Service
@Transactional
@RequiredArgsConstructor
public class AttendService {

    private final AttendRepository attendRepository;
    private final MeetingRepository meetingRepository;
    private final UserServiceClient userServiceClient;
    @Value("${secret.key}")
    private String secretKey;

    public AddAttendResponse addAttend(Long meetingId, String token) {
        try {
            String username = getUsername(token.replace("Bearer ",""));
            UserResponse userResponse = userServiceClient.fetchUsername(username,token);
            if(!userResponse.getCode().equals("SU")) throw new NoFoundUserException("No User!");
            Meeting meeting = meetingRepository.findById(meetingId).orElseThrow(() -> new NoFoundMeetingException("No Meeting!"));


            if(meeting.getCreatedById()  == userResponse.getId()){
                throw new AutoAttendException("Meeting opener attend auto");
            }

            Attend attend = Attend.builder()
                    .accepted(false)
                    .attendById(userResponse.getId())
                    .date(LocalDateTime.now())
                    .meeting(meeting)
                    .build();

            attendRepository.save(attend);

            return AddAttendResponse.builder()
                    .code(successCode)
                    .message(successMessage)
                    .build();

        }catch (NoFoundUserException e){
            return AddAttendResponse.builder()
                    .code(noFoundUserCode)
                    .message(noFoundUserMessage)
                    .build();
        }catch (NoFoundMeetingException e){
            return AddAttendResponse.builder()
                    .code(noFoundMeetingCode)
                    .message(noFoundMeetingMessage)
                    .build();
        }catch (AutoAttendException e){
            return AddAttendResponse.builder()
                    .code(autoAttendCode)
                    .message(autoAttendMessage)
                    .build();
        }
        catch (Exception e){
            return AddAttendResponse.builder()
                    .code(failCode)
                    .message(failMessage)
                    .build();
        }
    }

    public DisAttendResponse disAttend(Long meetingId, Long attendId, String token) {
        try {
            String username = getUsername(token.replace("Bearer ",""));
            UserResponse userResponse = userServiceClient.fetchUsername(username,token);
            if(!userResponse.getCode().equals("SU")) throw new NoFoundUserException("No User!");
            Meeting meeting = meetingRepository.findById(meetingId).orElseThrow(() -> new NoFoundMeetingException("No Meeting!"));
            Attend attend = attendRepository.findById(attendId).orElse(null);
            if(attend == null){
                throw new NoFoundAttendException("no found attend!");
            }
            if(meeting.getCreatedById()  == userResponse.getId()){
                throw new NoDisAttendException("meeting opener cannot disAttend!");
            }
            attendRepository.delete(attend);
            return DisAttendResponse.builder()
                    .code(successCode)
                    .message(successMessage)
                    .build();


        }catch (NoFoundUserException e){
            return DisAttendResponse.builder()
                    .code(noFoundUserCode)
                    .message(noFoundUserMessage)
                    .build();
        }catch (NoFoundMeetingException e){
            return DisAttendResponse.builder()
                    .code(noFoundMeetingCode)
                    .message(noFoundMeetingMessage)
                    .build();

        }catch (NoFoundAttendException e){
            return DisAttendResponse.builder()
                    .code(noFoundAttendCode)
                    .message(noFoundAttendMessage)
                    .build();
        }catch (NoDisAttendException e){
            return DisAttendResponse.builder()
                    .code(noDisAttendCode)
                    .message(noDisAttendMessage)
                    .build();
        }
        catch (Exception e){
            return DisAttendResponse.builder()
                    .code(failCode)
                    .message(failMessage)
                    .build();
        }
    }

    public PermitAttendResponse permitAttend(Long meetingId, Long attendId, String token) {
        try {
            String username = getUsername(token.replace("Bearer ",""));
            UserResponse userResponse = userServiceClient.fetchUsername(username,token);
            if(!userResponse.getCode().equals("SU")) throw new NoFoundUserException("No User!");
            Meeting meeting = meetingRepository.findById(meetingId).orElseThrow(() -> new NoFoundMeetingException("No Meeting!"));
            Attend attend = attendRepository.findById(attendId).orElseThrow(() -> new NoFoundAttendException("No Attend!"));
            if(attend.getAccepted() == true){
                throw new AlreadyAttendException("user already attend this meeting");
            }

            attend.setAccepted(true);
            return PermitAttendResponse.builder()
                    .code(successCode)
                    .message(successMessage)
                    .build();
        }catch (NoFoundUserException e){
            return PermitAttendResponse.builder()
                    .code(noFoundUserCode)
                    .message(noFoundUserMessage)
                    .build();
        }catch (NoFoundAttendException e){
            return PermitAttendResponse.builder()
                    .code(noFoundAttendCode)
                    .message(noFoundAttendMessage)
                    .build();
        }catch (AlreadyAttendException e){
            return PermitAttendResponse.builder()
                    .code(alreadyAttendCode)
                    .message(alreadyAttendMessage)
                    .build();
        }catch (Exception e){
            return PermitAttendResponse.builder()
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
