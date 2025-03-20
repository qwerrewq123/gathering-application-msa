package gathering.msa.recommend.service.eventhandler;

import event.Event;
import event.EventType;
import event.payload.MeetingDisAttendEventPayload;
import event.payload.MeetingUpdateEventPayload;
import gathering.msa.recommend.repository.GatheringMeetingCountRepository;
import gathering.msa.recommend.utils.TimeCalculatorUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MeetingDisAttendEventHandler implements EventHandler<MeetingDisAttendEventPayload>{
    private final GatheringMeetingCountRepository gatheringMeetingCountRepository;
    @Override
    public void handle(Event<MeetingDisAttendEventPayload> event) {
        MeetingDisAttendEventPayload payload = event.getPayload();
        Long gatheringId = payload.getGatheringId();
        gatheringMeetingCountRepository.createOrUpdate(gatheringId, TimeCalculatorUtils.calculateDurationToMidnight());
    }

    @Override
    public boolean supports(Event<MeetingDisAttendEventPayload> event) {
        return EventType.MEETING_DIS_ATTEND == event.getType();
    }

    @Override
    public Long findGatheringId(Event<MeetingDisAttendEventPayload> event) {
        return event.getPayload().getGatheringId();
    }
}
