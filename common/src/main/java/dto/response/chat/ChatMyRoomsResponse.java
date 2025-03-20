package dto.response.chat;

import dto.response.like.LikeResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMyRoomsResponse {
    private String code;
    private String message;
    private List<ChatMyRoomsResponseElement> elements;
    private boolean hasNext;

    public static ChatMyRoomsResponse of(String code, String message, List<ChatMyRoomsResponseElement> elements, boolean hasNext) {
        return new ChatMyRoomsResponse(code, message, elements, hasNext);
    }
}
