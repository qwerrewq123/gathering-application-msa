package dto.response.meeting;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class DeleteMeetingResponse {
    private String code;
    private String message;
    public static DeleteMeetingResponse of(String code, String message) {
        return new DeleteMeetingResponse(code, message);
    }
}
