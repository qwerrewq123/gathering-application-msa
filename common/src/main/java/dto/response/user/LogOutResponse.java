package dto.response.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class LogOutResponse {

    private String code;
    private String message;
}
