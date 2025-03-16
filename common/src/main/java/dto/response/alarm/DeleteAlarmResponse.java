package dto.response.alarm;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DeleteAlarmResponse {

    private String code;
    private String message;

    public static DeleteAlarmResponse of(String code, String message) {
        return new DeleteAlarmResponse(code, message);
    }
}
