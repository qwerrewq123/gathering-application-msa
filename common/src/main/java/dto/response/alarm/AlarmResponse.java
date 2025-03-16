package dto.response.alarm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class AlarmResponse {

    private String content;
    private LocalDateTime date;
    private Boolean checked;
}
