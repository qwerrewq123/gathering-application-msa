package gathering.msa.recommend.service.eventhandler;

import event.Event;
import event.EventType;
import event.payload.MeetingDeleteEventPayload;
import event.payload.MeetingDisAttendEventPayload;
import gathering.msa.recommend.repository.GatheringMeetingCreateRepository;
import gathering.msa.recommend.utils.TimeCalculatorUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MeetingDeletedEventHandler implements EventHandler<MeetingDeleteEventPayload>{
    private final GatheringMeetingCreateRepository gatheringMeetingCreateRepository;
    @Override
    public void handle(Event<MeetingDeleteEventPayload> event) {
        MeetingDeleteEventPayload payload = event.getPayload();
        Long gatheringId = payload.getGatheringId();
        gatheringMeetingCreateRepository.createOrUpdate(gatheringId, TimeCalculatorUtils.calculateDurationToMidnight());
    }

    @Override
    public boolean supports(Event<MeetingDeleteEventPayload> event) {
        return EventType.MEETING_DELETED == event.getType();
    }

    @Override
    public Long findGatheringId(Event<MeetingDeleteEventPayload> event) {
        return event.getPayload().getGatheringId();
    }
}
