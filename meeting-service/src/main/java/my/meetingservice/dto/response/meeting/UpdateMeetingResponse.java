package my.meetingservice.dto.response.meeting;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateMeetingResponse {

    private String code;
    private String message;
}
