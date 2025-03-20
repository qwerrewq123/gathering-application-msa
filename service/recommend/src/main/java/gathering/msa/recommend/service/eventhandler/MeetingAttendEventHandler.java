package gathering.msa.recommend.service.eventhandler;

import event.Event;
import event.EventType;
import event.payload.MeetingAttendEventPayload;
import event.payload.MeetingDisAttendEventPayload;
import gathering.msa.recommend.repository.GatheringMeetingCountRepository;
import gathering.msa.recommend.utils.TimeCalculatorUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MeetingAttendEventHandler implements EventHandler<MeetingAttendEventPayload>{
    private final GatheringMeetingCountRepository gatheringMeetingCountRepository;
    @Override
    public void handle(Event<MeetingAttendEventPayload> event) {
        MeetingAttendEventPayload payload = event.getPayload();
        Long gatheringId = payload.getGatheringId();
        gatheringMeetingCountRepository.createOrUpdate(gatheringId, TimeCalculatorUtils.calculateDurationToMidnight());
    }

    @Override
    public boolean supports(Event<MeetingAttendEventPayload> event) {
        return EventType.MEETING_ATTEND == event.getType();
    }

    @Override
    public Long findGatheringId(Event<MeetingAttendEventPayload> event) {
        return event.getPayload().getGatheringId();
    }
}
