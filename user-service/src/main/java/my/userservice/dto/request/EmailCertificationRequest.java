package my.userservice.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmailCertificationRequest {

    private String email;
}
