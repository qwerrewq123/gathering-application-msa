package my.alarmservice.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AlarmResponse {

    private Long id;
    private String content;
    private LocalDateTime date;
    private Boolean checked;
    private Long userId;
}
