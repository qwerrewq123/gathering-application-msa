package my.meetingservice.dto.response.meeting;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AddMeetingResponse {

    private String code;
    private String message;
}
