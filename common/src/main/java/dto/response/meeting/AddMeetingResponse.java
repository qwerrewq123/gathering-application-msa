package dto.response.meeting;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class AddMeetingResponse {
    private String code;
    private String message;

    public static AddMeetingResponse of(String code, String message) {
        return new AddMeetingResponse(code, message);
    }
}
