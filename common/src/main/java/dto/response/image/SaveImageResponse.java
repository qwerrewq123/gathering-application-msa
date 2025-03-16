package dto.response.image;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SaveImageResponse {
    private String code;
    private String message;
    private List<Long> ids = new ArrayList<>();

    public static SaveImageResponse of(String code, String message, List<Long> images) {
        return new SaveImageResponse(code, message, images);
    }
}
