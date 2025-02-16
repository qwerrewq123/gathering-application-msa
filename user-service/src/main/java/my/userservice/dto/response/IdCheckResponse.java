package my.userservice.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IdCheckResponse {

    private String code;
    private String message;
}
