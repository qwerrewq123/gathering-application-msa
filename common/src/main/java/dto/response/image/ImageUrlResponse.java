package dto.response.image;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageUrlResponse {
    private String code;
    private String message;
    private List<String> urls;
    public static ImageUrlResponse of(String code, String message, List<String> urls) {
        return new ImageUrlResponse(code, message, urls);
    }
}
