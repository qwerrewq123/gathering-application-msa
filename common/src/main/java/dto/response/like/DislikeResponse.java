package dto.response.like;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class DislikeResponse {

    private String code;
    private String message;

    public static DislikeResponse of(String code, String message) {
        return new DislikeResponse(code, message);
    }
}
