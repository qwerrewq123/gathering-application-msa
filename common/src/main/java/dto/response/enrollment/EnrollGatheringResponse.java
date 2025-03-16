package dto.response.enrollment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class EnrollGatheringResponse {
    private String code;
    private String message;

    public static EnrollGatheringResponse of(String code, String message) {
        return new EnrollGatheringResponse(code, message);
    }
}
