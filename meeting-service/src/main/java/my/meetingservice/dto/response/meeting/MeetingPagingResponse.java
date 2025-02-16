package my.meetingservice.dto.response.meeting;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;

@Data
@Builder
public class MeetingPagingResponse {

    private String code;
    private String message;
    private Page<MeetingListResponse> meetingListResponses;
}
