package my.gatheringservice.enrollment.service;

import feign.FeignException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.gatheringservice.client.UserServiceClient;
import my.gatheringservice.dto.response.DisEnrollGatheringResponse;
import my.gatheringservice.dto.response.EnrollGatheringResponse;
import my.gatheringservice.dto.response.user.UserResponse;
import my.gatheringservice.enrollment.Enrollment;
import my.gatheringservice.enrollment.repository.EnrollmentRepository;
import my.gatheringservice.exception.AlreadyEnrollmentException;
import my.gatheringservice.exception.NoEnrollmentException;
import my.gatheringservice.exception.NotFoundGatheringException;
import my.gatheringservice.gathering.Gathering;
import my.gatheringservice.gathering.repository.GatheringRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDateTime;

import static my.gatheringservice.util.GatheringConst.*;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class EnrollmentService {

    private final UserServiceClient userServiceClient;
    private final GatheringRepository gatheringRepository;
    private final EnrollmentRepository enrollmentRepository;
    @Value("${secret.key}")
    private String secretKey;

    public EnrollGatheringResponse enrollGathering(Long gatheringId, String token) {

        try {
            String username = getUsername(token.replace("Bearer ",""));
            UserResponse userResponse = userServiceClient.fetchUsername(username,token);
            Long userId = userResponse.getId();
            Gathering gathering = gatheringRepository.findById(gatheringId).orElseThrow(() -> new NotFoundGatheringException("Not Found Gathering!!"));
            Enrollment enrollment = enrollmentRepository.fetchEnrollment(gatheringId, userId).orElse(null);
            if(enrollment == null){
                throw new AlreadyEnrollmentException("already enrolled");

            }

            Enrollment.builder()
                    .date(LocalDateTime.now())
                    .enrolledBy(userId)
                    .gathering(gathering)
                    .accepted(true)
                    .build();

            return EnrollGatheringResponse.builder()
                    .code(successCode)
                    .message(successMessage)
                    .build();


        }catch (NoEnrollmentException e){
            return EnrollGatheringResponse.builder()
                    .code(notFoundEnrollmentCode)
                    .message(notFoundEnrollmentMessage)
                    .build();

        }catch (AlreadyEnrollmentException e){
            return EnrollGatheringResponse.builder()
                    .code(alreadyEnrollmentCode)
                    .message(alreadyEnrollmentMessage)
                    .build();

        }catch (FeignException e){
            return EnrollGatheringResponse.builder()
                    .code(feignFailCode)
                    .message(feignFailMessage)
                    .build();
        }
        catch (Exception e){
            return EnrollGatheringResponse.builder()
                    .code(failCode)
                    .message(failMessage)
                    .build();
        }

    }

    public DisEnrollGatheringResponse disEnrollGathering(Long gatheringId, String token) {

        try {
            String username = getUsername(token.replace("Bearer ",""));
            UserResponse userResponse = userServiceClient.fetchUsername(username,token);
            Long userId = userResponse.getId();
            Gathering gathering = gatheringRepository.findById(gatheringId).orElseThrow(() -> new NotFoundGatheringException("Not Found Gathering!!"));
            Enrollment enrollment = enrollmentRepository.fetchAcceptedEnrollment(gatheringId, userId).orElseThrow(()->new NoEnrollmentException("enrollment not exist"));


            gathering.getEnrollments().remove(enrollment);
            enrollmentRepository.delete(enrollment);

            return DisEnrollGatheringResponse.builder()
                    .code(successCode)
                    .message(successMessage)
                    .build();
        }catch (NoEnrollmentException e) {
            return DisEnrollGatheringResponse.builder()
                    .code(notFoundEnrollmentCode)
                    .message(notFoundEnrollmentMessage)
                    .build();

        }catch (FeignException e){
            return DisEnrollGatheringResponse.builder()
                    .code(feignFailCode)
                    .message(feignFailMessage)
                    .build();
        }
        catch (Exception e){
            return DisEnrollGatheringResponse.builder()
                    .code(failCode)
                    .message(failMessage)
                    .build();
        }
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


        }catch(Exception exception){
            exception.printStackTrace();
            return null;

        }

        return subject;
    }
}
