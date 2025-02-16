package my.meetingservice.dto.response.meeting;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class MeetingListResponse {


    private Long id;
    private String title;
    private String createdBy;
    private LocalDateTime boardDate;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String content;
}
