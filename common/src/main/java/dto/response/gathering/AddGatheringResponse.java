package dto.response.gathering;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class AddGatheringResponse {
    private String code;
    private String message;

    public static AddGatheringResponse of(String code, String message) {
        return new AddGatheringResponse(code, message);
    }
}
