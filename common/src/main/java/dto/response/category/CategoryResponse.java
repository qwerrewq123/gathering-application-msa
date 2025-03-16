package dto.response.category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryResponse {
    private String code;
    private String message;
    private Long id;

    public static CategoryResponse of(String code, String message, Long id) {
        return new CategoryResponse(code, message, id);
    }
}
