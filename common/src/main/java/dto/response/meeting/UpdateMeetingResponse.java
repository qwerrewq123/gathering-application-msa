package dto.response.meeting;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class UpdateMeetingResponse {
    private String code;
    private String message;
    public static UpdateMeetingResponse of(String code, String message) {
        return new UpdateMeetingResponse(code, message);
    }
}
