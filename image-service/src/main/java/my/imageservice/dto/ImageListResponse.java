package my.imageservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ImageListResponse {

    private String code;
    private String message;
    private List<ImageListElement> imageListElements;
}
