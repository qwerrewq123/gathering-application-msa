package my.gatheringservice.dto.response.gathering;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GatheringPagingResponse {


    private String code;
    private String message;
    private Page<GatheringPagingElement> page;

}
