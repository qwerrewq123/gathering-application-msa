package dto.response.attend;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class AddAttendResponse {
    private String code;
    private String message;

    public static AddAttendResponse of(String code, String message) {
        return new AddAttendResponse(code, message);
    }
}
