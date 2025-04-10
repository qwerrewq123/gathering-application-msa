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
public class GatheringDisEnrollmentEventPayload implements EventPayload {
    private Long id;
    private Boolean accepted;
    private Long gatheringId;
    private Long userId;
    private LocalDateTime date;
}
