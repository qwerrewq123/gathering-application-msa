package dto.response.alarm;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CheckAlarmResponse {

    private String code;
    private String message;

    public static CheckAlarmResponse of(String code, String message){
        return new CheckAlarmResponse(code, message);
    }
}
