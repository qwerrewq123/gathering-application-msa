package my.gatheringservice.dto.response.gathering;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateGatheringResponse {

    private String code;
    private String message;
}
