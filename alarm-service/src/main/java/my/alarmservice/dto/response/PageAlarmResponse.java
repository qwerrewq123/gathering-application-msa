package my.alarmservice.dto.response;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@Builder
public class PageAlarmResponse {

    private String code;
    private String message;
    private Page<AlarmResponse> alarmResponses;
}
