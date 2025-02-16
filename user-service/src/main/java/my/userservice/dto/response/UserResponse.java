package my.userservice.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponse {

    private String code;
    private String message;
    private Long id;
    private String username;
    private String email;
    private String address;
    private Integer age;
    private String hobby;
    private Long imageId;
}
