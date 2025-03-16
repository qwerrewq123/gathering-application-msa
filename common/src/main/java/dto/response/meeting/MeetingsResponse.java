package dto.response.meeting;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class MeetingsResponse {

    private String code;
    private String message;
    private List<MeetingsPage> elements = new ArrayList<>();
    private boolean hasNext;
}
