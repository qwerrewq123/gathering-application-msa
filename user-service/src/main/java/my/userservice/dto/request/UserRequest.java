package my.userservice.dto.request;


import lombok.Builder;
import lombok.Data;
import my.userservice.dto.response.ImageResponse;
import my.userservice.user.User;
import org.springframework.security.crypto.password.PasswordEncoder;

@Data
@Builder
public class UserRequest {

    private String username;
    private String password;
    private String email;
    private String address;
    private Integer age;
    private String hobby;

    public static User toUser(UserRequest userRequest, PasswordEncoder passwordEncoder, ImageResponse imageResponse){
        return User.builder()
                .age(userRequest.getAge())
                .email(userRequest.getEmail())
                .hobby(userRequest.getHobby())
                .address(userRequest.getAddress())
                .username(userRequest.getUsername())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .imageId(imageResponse.getId() != null ? imageResponse.getId() : null)
                .build();
    }

}
