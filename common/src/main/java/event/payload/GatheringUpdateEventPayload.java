package event.payload;

import event.EventPayload;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GatheringUpdateEventPayload implements EventPayload {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime registerDate;
    private Long categoryId;
    private Long userId;
    private Long imageId;
}
