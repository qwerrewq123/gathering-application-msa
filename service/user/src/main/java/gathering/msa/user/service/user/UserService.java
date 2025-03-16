package gathering.msa.user.service.user;

import dto.request.user.*;
import dto.response.image.SaveImageResponse;
import dto.response.user.*;
import exception.image.ImageUploadFailException;
import exception.user.*;
import gathering.msa.user.async.AsyncService;
import gathering.msa.user.client.ImageServiceClient;
import gathering.msa.user.entity.user.Role;
import gathering.msa.user.entity.user.User;
import gathering.msa.user.provider.JwtProvider;
import gathering.msa.user.repository.user.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static gathering.msa.user.util.CookieUtil.getCookie;
import static util.ConstClass.*;


@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final ImageServiceClient imageServiceClient;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final AsyncService asyncService;
    @Value("${jwt.refresh.expiration}")
    private int refreshExpiration;
    @Value("${jwt.secretKey}")
    private String secretKey;


    public IdCheckResponse idCheck(IdCheckRequest idCheckRequest) {

        boolean idCheck = !userRepository.existsByUsername(idCheckRequest.getUsername());
        if(!idCheck) throw new ExistUserException("user Exist!!");
        return IdCheckResponse.builder()
                .code(SUCCESS_CODE)
                .message(SUCCESS_MESSAGE)
                .build();
    }
    public NicknameCheckResponse nicknameCheck(NicknameCheckRequest nicknameCheckRequest) {
        boolean nicknameCheck = !userRepository.existsByNickname(nicknameCheckRequest.getNickname());
        if(!nicknameCheck) throw new ExistUserException("user Exist!!");
        return NicknameCheckResponse.builder()
                .code(SUCCESS_CODE)
                .message(SUCCESS_MESSAGE)
                .build();

    }

    public SignUpResponse signUp(UserRequest userRequest, MultipartFile file){

        SaveImageResponse saveImageResponse = imageServiceClient.saveImage(List.of(file), null);
        if(saveImageResponse.getCode().equals(UPLOAD_FAIL_CODE)) throw new ImageUploadFailException("upload image failed!");
        User user = User.builder()
                .age(userRequest.getAge())
                .email(userRequest.getEmail())
                .hobby(userRequest.getHobby())
                .address(userRequest.getAddress())
                .username(userRequest.getUsername())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .role(Role.USER)
                .imageId(saveImageResponse.getIds().getFirst())
                .nickname(userRequest.getNickname())
                .build();
        userRepository.save(user);
        return SignUpResponse.builder()
                .code(SUCCESS_CODE)
                .message(SUCCESS_MESSAGE)
                .build();

    }

    public SignInResponse signIn(SignInRequest signInRequest, HttpServletResponse response) {

            User user = userRepository.findByUsername(signInRequest.getUsername()).orElseThrow(() -> new NotFoundUserException("not Found User"));
            boolean matches = passwordEncoder.matches(signInRequest.getPassword(), user.getPassword());
            if(!matches){
                throw new UnCorrectPasswordException("doesn't match Password!");
            }
            String accessToken = jwtProvider.createAccessToken(user.getUsername(),user.getRole().toString());
            String refreshToken = jwtProvider.createRefreshToken(user.getUsername(),user.getRole().toString());
            Cookie cookie = getCookie("refreshToken", refreshToken,refreshExpiration);
            response.addCookie(cookie);
            user.changeRefreshToken(refreshToken);
            return SignInResponse.builder()
                    .code(SUCCESS_CODE)
                    .message(SUCCESS_MESSAGE)
                    .accessToken(accessToken)
                    .build();
    }

    public EmailCertificationResponse emailCertification(EmailCertificationRequest emailCertificationRequest) {

            List<User> users = userRepository.findByEmail(emailCertificationRequest.getEmail());
            if(users.isEmpty()) throw new NotFoundEmailExeption("Not Found Email");
            if(users.size()>1) throw new DuplicateEmailExeption("Duplicate Email");
            asyncService.asyncTask(emailCertificationRequest);
            return EmailCertificationResponse.builder()
                        .code(SUCCESS_CODE)
                        .message(SUCCESS_MESSAGE)
                        .build();
    }


    public UserResponse fetchUserByUsername(String username) {
        User foundUser = userRepository.findByUsername(username).orElseThrow(() -> new NotFoundUserException("not Found User"));

        return UserResponse.builder()
                .code(SUCCESS_CODE)
                .message(SUCCESS_MESSAGE)
                .id(foundUser.getId())
                .username(foundUser.getUsername())
                .password(foundUser.getPassword())
                .email(foundUser.getEmail())
                .address(foundUser.getAddress())
                .age(foundUser.getAge())
                .hobby(foundUser.getHobby())
                .role(foundUser.getRole().toString())
                .nickname(foundUser.getNickname())
                .imageId(foundUser.getImageId())
                .build();
    }

    //TODO : 로직처리하기
    public GenerateTokenResponse generateToken(String refreshToken) {

        return null;
    }


    public UserResponses fetchUserByIds(List<Long> ids) {
        List<User> users = userRepository.findAllById(ids);
        List<UserResponsesElement> elements = users.stream()
                .map(foundUser -> UserResponsesElement.builder()
                        .id(foundUser.getId())
                        .username(foundUser.getUsername())
                        .password(foundUser.getPassword())
                        .email(foundUser.getEmail())
                        .address(foundUser.getAddress())
                        .age(foundUser.getAge())
                        .hobby(foundUser.getHobby())
                        .role(foundUser.getRole().toString())
                        .nickname(foundUser.getNickname())
                        .imageId(foundUser.getImageId())
                        .build())
                .toList();
        return UserResponses.builder()
                .code(SUCCESS_CODE)
                .message(SUCCESS_MESSAGE)
                .elements(elements)
                .build();
    }
}
