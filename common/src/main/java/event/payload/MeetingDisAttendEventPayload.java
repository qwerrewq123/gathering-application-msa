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
public class MeetingDisAttendEventPayload implements EventPayload {
    private Long id;
    private Long gatheringId;
    private Boolean accepted;
    private Long meetingId;
    private Long userId;
    private LocalDateTime date;
}
