package gathering.msa.user.controller.user;

import dto.request.user.*;
import dto.response.user.*;
import gathering.msa.user.async.AsyncService;
import gathering.msa.user.service.user.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AsyncService asyncService;

    @PostMapping("/auth/id-check")
    public ResponseEntity<IdCheckResponse> idCheck(@RequestBody IdCheckRequest idCheckRequest) {

        IdCheckResponse idCheckResponse = userService.idCheck(idCheckRequest);
        return new ResponseEntity<>(idCheckResponse, HttpStatus.OK);
    }
    @PostMapping("/auth/nickname-check")
    public ResponseEntity<NicknameCheckResponse> nicknameCheck(@RequestBody NicknameCheckRequest nicknameCheckRequest) {

        NicknameCheckResponse nicknameCheckResponse = userService.nicknameCheck(nicknameCheckRequest);
        return new ResponseEntity<>(nicknameCheckResponse, HttpStatus.OK);
    }

    @PostMapping(value = "/auth/sign-up",consumes = {
            MediaType.MULTIPART_FORM_DATA_VALUE,
            MediaType.APPLICATION_JSON_VALUE
    })
    public ResponseEntity<SignUpResponse> signUp(@RequestPart("userRequest") UserRequest userRequest, @RequestPart(required = false,name = "file") MultipartFile file) throws IOException {

        SignUpResponse signUpResponse = userService.signUp(userRequest, file);
        return new ResponseEntity<>(signUpResponse, HttpStatus.OK);
    }
    @GetMapping("/user/username")
    public ResponseEntity<UserResponse> fetchUserByUsername(@RequestParam("username") String username) {
        UserResponse userResponse = userService.fetchUserByUsername(username);
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }
    @GetMapping("/user/id")
    public ResponseEntity<UserResponses> fetchUserByIds(@RequestParam("id") List<Long> ids) {
        UserResponses userResponses = userService.fetchUserByIds(ids);
        return new ResponseEntity<>(userResponses, HttpStatus.OK);
    }

    @PostMapping("/auth/sign-in")
    public ResponseEntity<SignInResponse> signIn(@RequestBody SignInRequest signInRequest, HttpServletResponse response) {

        SignInResponse signInResponse = userService.signIn(signInRequest,response);
        return new ResponseEntity<>(signInResponse,HttpStatus.OK);
    }

    @PostMapping(value = "/auth/email-certification")
    public ResponseEntity<EmailCertificationResponse> emailCertification(@RequestBody EmailCertificationRequest emailCertificationRequest){

        EmailCertificationResponse emailCertificationResponse = userService.emailCertification(emailCertificationRequest);
        asyncService.asyncTask(emailCertificationRequest);
        return new ResponseEntity<>(emailCertificationResponse,HttpStatus.OK);
    }

    @PostMapping(value = "/auth/generateToken")
    public ResponseEntity<GenerateTokenResponse> generateToken(@CookieValue(value = "refreshToken", required = false) String refreshToken){
        GenerateTokenResponse generateTokenResponse = userService.generateToken(refreshToken);
        return new ResponseEntity<>(generateTokenResponse,HttpStatus.OK);
    }
}
