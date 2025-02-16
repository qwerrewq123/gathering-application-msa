package my.meetingservice.dto.request;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AddMeetingRequest {

    private String title;
    private String content;
    private LocalDateTime startDate;
    private LocalDateTime endDate;


}