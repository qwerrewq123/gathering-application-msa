package dto.response.alarm;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddAlarmResponse {

    private String code;
    private String message;

    public static AddAlarmResponse of(String code, String message){
        return new AddAlarmResponse(code, message);
    }
}
