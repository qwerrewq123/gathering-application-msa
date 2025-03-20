package event.payload;

import event.EventPayload;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageCreatedEventPayload implements EventPayload {
    private Long roomId;
    private Long gatheringId;
    private String content;
    private Long userId;
}
