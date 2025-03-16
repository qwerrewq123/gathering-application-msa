package dto.response.image;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GatheringImageResponse {
    private String code;
    private String message;
    private List<String> urls = new ArrayList<>();

    public static GatheringImageResponse of(String code, String message, List<String> urls) {
        return GatheringImageResponse.builder()
                .code(code)
                .message(message)
                .urls(urls)
                .build();
    }
}
