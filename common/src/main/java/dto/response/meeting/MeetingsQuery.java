package dto.response.meeting;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class MeetingsQuery {

    private Long id;
    private String title;
    private Long createdById;
    private LocalDateTime boardDate;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String content;
    private int count;
    private Long imageId;
}
