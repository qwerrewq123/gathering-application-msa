package dto.response.like;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class LikeResponse {

    private String code;
    private String message;

    public static LikeResponse of(String code, String message) {
        return new LikeResponse(code, message);
    }
}
