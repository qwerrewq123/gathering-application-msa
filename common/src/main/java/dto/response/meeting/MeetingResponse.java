package dto.response.meeting;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class MeetingResponse {

    private String code;
    private String message;
    private Long id;
    private String title;
    private String createdBy;
    private List<String> attendedBy;
    private LocalDateTime boardDate;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String content;
    private String url;

}
