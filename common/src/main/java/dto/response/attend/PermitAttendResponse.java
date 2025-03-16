package dto.response.attend;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class PermitAttendResponse {
    private String code;
    private String message;

    public static PermitAttendResponse of(String code, String message) {
        return new PermitAttendResponse(code, message);
    }
}
