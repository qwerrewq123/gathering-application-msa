package dto.response.gathering;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class UpdateGatheringResponse {
    private String code;
    private String message;

    public static UpdateGatheringResponse of(String code, String message) {
        return new UpdateGatheringResponse(code, message);
    }
}
