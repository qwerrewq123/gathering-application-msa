package dto.response.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatRoomsResponse {
    private String code;
    private String message;
    private List<ChatRoomsResponseElement> content;
    private boolean hasNext;

    public static ChatRoomsResponse of(String code, String message, List<ChatRoomsResponseElement> content,boolean hasNext) {
        return new ChatRoomsResponse(code,message,content,hasNext);
    }


}
