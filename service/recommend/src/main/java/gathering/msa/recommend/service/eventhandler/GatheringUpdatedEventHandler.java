package gathering.msa.recommend.service.eventhandler;

import event.Event;
import event.EventType;
import event.payload.GatheringCreatedEventPayload;
import event.payload.GatheringUpdateEventPayload;
import event.payload.MeetingDisAttendEventPayload;
import gathering.msa.recommend.repository.GatheringCreateRepository;
import gathering.msa.recommend.utils.TimeCalculatorUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GatheringUpdatedEventHandler implements EventHandler<GatheringUpdateEventPayload>{
    private final GatheringCreateRepository gatheringCreateRepository;
    @Override
    public void handle(Event<GatheringUpdateEventPayload> event) {
        GatheringUpdateEventPayload payload = event.getPayload();
        Long gatheringId = payload.getId();
        gatheringCreateRepository.createOrUpdate(gatheringId, TimeCalculatorUtils.calculateDurationToMidnight());
    }

    @Override
    public boolean supports(Event<GatheringUpdateEventPayload> event) {
        return EventType.GATHERING_UPDATED == event.getType();
    }

    @Override
    public Long findGatheringId(Event<GatheringUpdateEventPayload> event) {
        return event.getPayload().getId();
    }
}
