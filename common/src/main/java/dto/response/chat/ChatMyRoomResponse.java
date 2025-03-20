package dto.response.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMyRoomResponse {
    private Long id;
    private String name;
    private int count;
    private Long userId;
    private boolean status;
    private long unReadCount;
}
