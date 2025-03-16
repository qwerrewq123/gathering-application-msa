package dto.response.attend;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class DisAttendResponse {
    private String code;
    private String message;

    public static DisAttendResponse of(String code, String message) {
        return new DisAttendResponse(code, message);
    }
}
