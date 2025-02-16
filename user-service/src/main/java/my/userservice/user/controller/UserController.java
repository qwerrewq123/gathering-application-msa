package my.userservice.user.controller;

import lombok.RequiredArgsConstructor;
import my.userservice.async.AsyncService;
import my.userservice.dto.request.EmailCertificationRequest;
import my.userservice.dto.request.IdCheckRequest;
import my.userservice.dto.request.SignInRequest;
import my.userservice.dto.request.UserRequest;
import my.userservice.dto.response.*;
import my.userservice.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AsyncService asyncService;




    @PostMapping("/auth/id-check")
    public ResponseEntity<IdCheckResponse> idCheck(@RequestBody IdCheckRequest idCheckRequest) {

            IdCheckResponse idCheckResponse = userService.idCheck(idCheckRequest);

            if (idCheckResponse.getCode().equals("SU")) {

                return new ResponseEntity<>(idCheckResponse, HttpStatus.OK);
            } else {

                return new ResponseEntity<>(idCheckResponse, HttpStatus.BAD_REQUEST);
            }

    }

    @PostMapping(value = "/auth/sign-up",consumes = {
            MediaType.MULTIPART_FORM_DATA_VALUE,
            MediaType.APPLICATION_JSON_VALUE
    })
    public ResponseEntity<SignUpResponse> signUp(@RequestPart("userRequest") UserRequest userRequest, @RequestParam("file") MultipartFile file){

        SignUpResponse signUpResponse = userService.signUp(userRequest, file);
        if(signUpResponse.getCode().equals("SU")){
            return new ResponseEntity<>(signUpResponse, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(signUpResponse, HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping("/auth/sign-in")
    public ResponseEntity<SignInResponse> signIn(@RequestBody SignInRequest signInRequest) {


        SignInResponse signInResponse = userService.signIn(signInRequest);
        if(signInResponse.getCode().equals("SU")){
            return new ResponseEntity<>(signInResponse,HttpStatus.OK);
        }else{
            return new ResponseEntity<>(signInResponse,HttpStatus.BAD_REQUEST);

        }




    }


    @PostMapping(value = "/auth/email-certification")
    public ResponseEntity<EmailCertificationResponse> emailCertification(@RequestBody EmailCertificationRequest emailCertificationRequest){


            EmailCertificationResponse emailCertificationResponse = userService.emailCertification(emailCertificationRequest);
            if(emailCertificationResponse.getCode().equals("SU")){
                asyncService.asyncTask(emailCertificationRequest);
                return new ResponseEntity<>(emailCertificationResponse,HttpStatus.OK);
            }else{
                return new ResponseEntity<>(emailCertificationResponse,HttpStatus.BAD_REQUEST);
            }
    }

    @GetMapping("/user")
    public ResponseEntity<UserResponse> fetchUsername(@RequestParam String username){
        UserResponse userResponse = userService.fetch(username);
        String code = userResponse.getCode();
        if(code.equals("SU")){
            return new ResponseEntity<>(userResponse,HttpStatus.OK);
        }else{
            return new ResponseEntity<>(userResponse,HttpStatus.BAD_REQUEST);

        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<UserResponse> fetchUserId(@PathVariable Long userId){
        UserResponse userResponse = userService.fetchUserId(userId);
        String code = userResponse.getCode();
        if(code.equals("SU")){
            return new ResponseEntity<>(userResponse,HttpStatus.OK);
        }else{
            return new ResponseEntity<>(userResponse,HttpStatus.BAD_REQUEST);

        }
    }

    @GetMapping("/users")
    public ResponseEntity<UserListResponse> fetchList(@RequestParam List<Long> userIdList){
        UserListResponse userResponseList = userService.fetchList(userIdList);

        if(userResponseList.getCode().equals("SU")){
            return new ResponseEntity<>(userResponseList, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(userResponseList, HttpStatus.BAD_REQUEST);

        }
    }
}
