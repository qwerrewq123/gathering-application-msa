package dto.response.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatRoomResponses {
    private String code;
    private String message;
    private boolean hasNext;
    private List<ChatRoomResponsesElement> elements = new ArrayList<>();

}
