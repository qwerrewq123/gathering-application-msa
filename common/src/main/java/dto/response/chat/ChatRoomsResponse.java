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
public class ChatRoomsResponse {
    private String code;
    private String message;
    private List<ChatRoomResponse> elements = new ArrayList<>();
    private boolean hasNext;
//    private Page<ChatRoomResponse> page;

//    public static ChatRoomsResponse of(String code, String message, Page<ChatRoomResponse> page) {
//        return new ChatRoomsResponse(code, message, page);
//    }
}
