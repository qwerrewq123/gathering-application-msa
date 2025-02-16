package my.userservice.user.service;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.userservice.client.ImageServiceClient;
import my.userservice.dto.request.EmailCertificationRequest;
import my.userservice.dto.request.IdCheckRequest;
import my.userservice.dto.request.SignInRequest;
import my.userservice.dto.request.UserRequest;
import my.userservice.dto.response.*;
import my.userservice.exception.NotFoundUserException;
import my.userservice.exception.UnCorrectPasswordException;
import my.userservice.provider.EmailProvider;
import my.userservice.provider.JwtProvider;
import my.userservice.user.User;
import my.userservice.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

import static my.userservice.util.UserConst.*;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final EmailProvider emailProvider;
    private final ImageServiceClient imageServiceClient;


    public IdCheckResponse idCheck(IdCheckRequest idCheckRequest) {
        Boolean exist = userRepository.existsByUsername(idCheckRequest.getUsername());
        if(!exist){
            return IdCheckResponse.builder()
                    .code(successCode)
                    .message(successMessage)
                    .build();
        }else{
            return IdCheckResponse.builder()
                    .code(existCode)
                    .message(existMessage)
                    .build();
        }

    }

    public SignUpResponse signUp(UserRequest userRequest, MultipartFile file) {
        try {

            ImageResponse imageResponse = imageServiceClient.upload(file);
            User user = UserRequest.toUser(userRequest,passwordEncoder,imageResponse);
            userRepository.save(user);


            return SignUpResponse.builder()
                    .code(successCode)
                    .message(successMessage)
                    .build();

        }catch (FeignException e){
            return SignUpResponse.builder()
                    .code(failSaveCode)
                    .message(failSaveMessage)
                    .build();
        }
    }

    public SignInResponse signIn(SignInRequest signInRequest) {


        try {
            User user = userRepository.findByUsername(signInRequest.getUsername()).orElseThrow(() -> new NotFoundUserException("not Found User"));
            boolean matches = passwordEncoder.matches(signInRequest.getPassword(), user.getPassword());
            checkPassword(matches);
            String token = jwtProvider.create(user.getUsername());

            return SignInResponse.builder()
                    .code(successCode)
                    .message(successMessage)
                    .token(token)
                    .build();

        }catch (NotFoundUserException e){
            return SignInResponse.builder()
                    .code(notFoundCode)
                    .message(notFoundMessage)
                    .build();


        }catch (UnCorrectPasswordException e){
            return SignInResponse.builder()
                    .code(unCorrectCode)
                    .message(unCorrectMessage)
                    .build();
        }
    }

    public EmailCertificationResponse emailCertification(EmailCertificationRequest emailCertificationRequest) {

        List<User> users = userRepository.findByEmail(emailCertificationRequest.getEmail());
        if(users.size() == 0){
            return EmailCertificationResponse.builder()
                    .code(notEmailCode)
                    .message(notEmailMessage)
                    .build();
        } else if (users.size() == 1) {
            return EmailCertificationResponse.builder()
                    .code(successCode)
                    .message(successMessage)
                    .build();
        }else{
            return EmailCertificationResponse.builder()
                    .code(duplicateEmailCode)
                    .message(duplicateEmailMessage)
                    .build();
        }


    }

    public UserResponse fetch(String username) {

        User user = null;
        try {
            user = userRepository.findByUsername(username).orElseThrow(() -> new NotFoundUserException("not Found User"));
            return toUserResponse(user,true);
        }catch (NotFoundUserException e){
            log.error("NotFoundUserException !!",e);
            return toUserResponse(user,false);
        }
    }

    public UserResponse fetchUserId(Long userId) {

        User user = null;
        try {
            user = userRepository.findById(userId).orElseThrow(() -> new NotFoundUserException("not Found User"));
            return toUserResponse(user,true);
        }catch (NotFoundUserException e){
            log.error("NotFoundUserException !!",e);
            return toUserResponse(user,false);

        }
    }

    public UserListResponse fetchList(List<Long> userIdList) {
        List<User> users =  userRepository.fetchList(userIdList);
        if(users.size()>0){
            List<UserListElement> collect = users.stream().map(u -> UserListElement.builder()
                            .id(u.getId())
                            .username(u.getUsername())
                            .email(u.getEmail())
                            .address(u.getAddress())
                            .age(u.getAge())
                            .hobby(u.getHobby())
                            .imageId(u.getImageId())
                            .build())
                    .collect(Collectors.toList());

            return UserListResponse.builder()
                    .users(collect)
                    .code(notFoundCode)
                    .message(notFoundMessage)
                    .build();


        }
        else{
            return UserListResponse.builder()
                    .code(notFoundCode)
                    .message(notFoundMessage)
                    .build();
        }

    }

    private void checkPassword(boolean matches) {
        if(!matches){
            throw new UnCorrectPasswordException("doesn't match Password!");
        }
    }

    private UserResponse toUserResponse(User user, boolean success){
        if(success){
            return UserResponse.builder()
                    .code(successCode)
                    .message(successMessage)
                    .id(user.getId())
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .address(user.getAddress())
                    .age(user.getAge())
                    .hobby(user.getHobby())
                    .imageId(user.getImageId())
                    .build();

        }else{
            return UserResponse.builder()
                    .code(notFoundCode)
                    .message(notFoundMessage)
                    .build();
        }
    }
}
