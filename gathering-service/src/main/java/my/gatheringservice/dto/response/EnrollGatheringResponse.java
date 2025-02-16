package my.gatheringservice.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EnrollGatheringResponse {

    private String code;
    private String message;
}
