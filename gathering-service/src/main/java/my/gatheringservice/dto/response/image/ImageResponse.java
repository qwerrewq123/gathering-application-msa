package my.gatheringservice.dto.response.image;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ImageResponse {

    private String code;
    private String message;
    private Long id;
    private String url;
}
