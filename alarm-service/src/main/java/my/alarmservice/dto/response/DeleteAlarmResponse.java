package my.alarmservice.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeleteAlarmResponse {

    private String code;
    private String message;
}
