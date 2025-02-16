package my.alarmservice.alarm.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import my.alarmservice.alarm.Alarm;
import my.alarmservice.alarm.repository.AlarmRepository;
import my.alarmservice.client.UserServiceClient;
import my.alarmservice.dto.request.AddAlarmRequest;
import my.alarmservice.dto.response.*;
import my.alarmservice.exception.AlarmCheckedException;
import my.alarmservice.exception.NoFoundAlarmException;
import my.alarmservice.exception.NoFoundUserException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDateTime;

import static my.alarmservice.util.AlarmConst.*;

@Service
@RequiredArgsConstructor
@Transactional
public class AlarmService {

    private final AlarmRepository alarmRepository;
    private final UserServiceClient userServiceClient;
    @Value("${secret.key}")
    private String secretKey;

    public AddAlarmResponse addAlarm(AddAlarmRequest addAlarmRequest, String token) {
        try {
            String username = getUsername(token.replace("Bearer ",""));
            UserResponse userResponse = userServiceClient.fetchUsername(username,token);
            if(userResponse == null){
                throw new NoFoundUserException("No user!");
            }

            Alarm alarm = Alarm.builder()
                    .date(LocalDateTime.now())
                    .content(addAlarmRequest.getContent())
                    .checked(false)
                    .userId(userResponse.getId())
                    .build();

            alarmRepository.save(alarm);

            return AddAlarmResponse.builder()
                    .code(successCode)
                    .message(successMessage)
                    .build();

        }catch (NoFoundUserException e){
            return AddAlarmResponse.builder()
                    .code(notFoundUserCode)
                    .message(notFoundUserMessage)
                    .build();
        }
    }

    public CheckAlarmResponse checkAlarm(Long alarmId, String token) {

        try {
            String username = getUsername(token.replace("Bearer ",""));
            UserResponse userResponse = userServiceClient.fetchUsername(username,token);

            if(userResponse.getCode().equals("NF")){
                throw new NoFoundUserException("No user!");
            }

            Alarm alarm = alarmRepository.findById(alarmId).orElseThrow(() -> {
                throw new NoFoundAlarmException("no Alarm");
            });

            if(alarm.getChecked() == true){
                throw new AlarmCheckedException("alarm already checked!");
            }
            alarm.setChecked(true);
            return CheckAlarmResponse.builder()
                    .code(successCode)
                    .message(successMessage)
                    .build();

        }catch (NoFoundUserException e){

            return CheckAlarmResponse.builder()
                    .code(notFoundUserCode)
                    .message(notFoundUserMessage)
                    .build();
        }catch (NoFoundAlarmException e){
            return CheckAlarmResponse.builder()
                    .code(notFoundAlarmCode)
                    .message(notFoundAlarmMessage)
                    .build();
        }catch (AlarmCheckedException e){
            return CheckAlarmResponse.builder()
                    .code(alarmCheckedCode)
                    .message(alarmCheckedMessage)
                    .build();
        }
    }

    public DeleteAlarmResponse deleteAlarm(Long alarmId, String token) {
        try {
            String username = getUsername(token.replace("Bearer ",""));

            UserResponse userResponse = userServiceClient.fetchUsername(username,token);
            if(userResponse == null){
                throw new NoFoundUserException("No user!");
            }

            Alarm alarm = alarmRepository.findById(alarmId).orElseThrow(() -> {
                throw new NoFoundAlarmException("no Alarm");
            });

            alarmRepository.delete(alarm);
            return DeleteAlarmResponse.builder()
                    .code(successCode)
                    .message(successMessage)
                    .build();
        }catch (NoFoundUserException e){
            return DeleteAlarmResponse.builder()
                    .code(notFoundUserCode)
                    .message(notFoundUserMessage)
                    .build();
        }catch (NoFoundAlarmException e){
            return DeleteAlarmResponse.builder()
                    .code(notFoundAlarmCode)
                    .message(notFoundAlarmCode)
                    .build();
        }
    }

    public PageAlarmResponse alarmList(int page, String token, Boolean checked) {
        try {
            String username = getUsername(token.replace("Bearer ",""));

            UserResponse userResponse = userServiceClient.fetchUsername(username,token);
            if (userResponse == null) {
                throw new NoFoundUserException("No user!");
            }

            if (checked == true) {
                PageRequest pageRequest = PageRequest.of(page - 1, 10);
                Page<Alarm> pageAlarm = alarmRepository.fetchCheckedAlarmPage(pageRequest,userResponse.getId());
                Page<AlarmResponse> pageAlarmResponse = pageAlarm.map(a ->
                        AlarmResponse.builder()
                                .id(a.getId())
                                .date(a.getDate())
                                .content(a.getContent())
                                .checked(a.getChecked())
                                .userId(a.getUserId())
                                .build());
                return PageAlarmResponse.builder()
                        .code(successCode)
                        .message(successMessage)
                        .alarmResponses(pageAlarmResponse)
                        .build();
            } else {
                PageRequest pageRequest = PageRequest.of(page - 1, 10);
                Page<Alarm> pageAlarm = alarmRepository.fetchUnCheckedAlarmPage(pageRequest,userResponse.getId());
                Page<AlarmResponse> pageAlarmResponse = pageAlarm.map(a ->
                        AlarmResponse.builder()
                                .id(a.getId())
                                .userId(a.getUserId())
                                .date(a.getDate())
                                .content(a.getContent())
                                .checked(a.getChecked())
                                .build());
                return PageAlarmResponse.builder()
                        .code(successCode)
                        .message(successMessage)
                        .alarmResponses(pageAlarmResponse)
                        .build();
            }
        } catch (NoFoundUserException e) {
            return PageAlarmResponse.builder()
                    .code(notFoundUserCode)
                    .message(notFoundUserMessage)
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
