package my.gatheringservice.dto.response.category;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryListResponse {

    private String code;
    private String message;
    private List<CategoryListElement> categoryLIstElements;
}
