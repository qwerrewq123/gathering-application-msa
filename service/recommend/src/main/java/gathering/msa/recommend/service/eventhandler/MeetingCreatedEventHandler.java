package gathering.msa.recommend.service.eventhandler;

import event.Event;
import event.EventType;
import event.payload.MeetingCreatedEventPayload;
import event.payload.MeetingDisAttendEventPayload;
import gathering.msa.recommend.repository.GatheringMeetingCreateRepository;
import gathering.msa.recommend.utils.TimeCalculatorUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MeetingCreatedEventHandler implements EventHandler<MeetingCreatedEventPayload>{
    private final GatheringMeetingCreateRepository gatheringMeetingCreateRepository;
    @Override
    public void handle(Event<MeetingCreatedEventPayload> event) {
        MeetingCreatedEventPayload payload = event.getPayload();
        Long gatheringId = payload.getGatheringId();
        gatheringMeetingCreateRepository.createOrUpdate(gatheringId, TimeCalculatorUtils.calculateDurationToMidnight());
    }

    @Override
    public boolean supports(Event<MeetingCreatedEventPayload> event) {
        return EventType.MEETING_CREATED == event.getType();
    }

    @Override
    public Long findGatheringId(Event<MeetingCreatedEventPayload> event) {
        return event.getPayload().getGatheringId();
    }
}
