package event.payload;

import event.EventPayload;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomCreatedEventPayload implements EventPayload {
    private Long roomId;
    private Long gatheringId;
    private Long userId;
}
