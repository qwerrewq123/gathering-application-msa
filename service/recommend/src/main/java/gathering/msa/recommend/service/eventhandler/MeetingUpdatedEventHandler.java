package gathering.msa.recommend.service.eventhandler;

import event.Event;
import event.EventType;
import event.payload.MeetingUpdateEventPayload;
import gathering.msa.recommend.repository.GatheringMeetingCreateRepository;
import gathering.msa.recommend.utils.TimeCalculatorUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MeetingUpdatedEventHandler implements EventHandler<MeetingUpdateEventPayload>{
    private final GatheringMeetingCreateRepository gatheringMeetingCreateRepository;
    @Override
    public void handle(Event<MeetingUpdateEventPayload> event) {
        MeetingUpdateEventPayload payload = event.getPayload();
        Long gatheringId = payload.getGatheringId();
        gatheringMeetingCreateRepository.createOrUpdate(gatheringId, TimeCalculatorUtils.calculateDurationToMidnight());
    }

    @Override
    public boolean supports(Event<MeetingUpdateEventPayload> event) {
        return EventType.MEETING_UPDATED == event.getType();
    }

    @Override
    public Long findGatheringId(Event<MeetingUpdateEventPayload> event) {
        return event.getPayload().getGatheringId();
    }
}
