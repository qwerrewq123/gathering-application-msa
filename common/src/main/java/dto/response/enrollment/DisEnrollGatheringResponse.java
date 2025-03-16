package dto.response.enrollment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class DisEnrollGatheringResponse {
    private String code;
    private String message;

    public static DisEnrollGatheringResponse of(String code, String message) {
        return new DisEnrollGatheringResponse(code, message);
    }
}
