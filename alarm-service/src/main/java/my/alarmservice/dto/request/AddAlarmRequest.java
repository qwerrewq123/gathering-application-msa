package my.alarmservice.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddAlarmRequest {

    private String content;
}
