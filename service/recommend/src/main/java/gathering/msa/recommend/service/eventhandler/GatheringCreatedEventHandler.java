package gathering.msa.recommend.service.eventhandler;

import event.Event;
import event.EventType;
import event.payload.GatheringCreatedEventPayload;
import event.payload.MeetingDisAttendEventPayload;
import gathering.msa.recommend.repository.GatheringCreateRepository;
import gathering.msa.recommend.utils.TimeCalculatorUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GatheringCreatedEventHandler implements EventHandler<GatheringCreatedEventPayload>{
    private final GatheringCreateRepository gatheringCreateRepository;
    @Override
    public void handle(Event<GatheringCreatedEventPayload> event) {
        GatheringCreatedEventPayload payload = event.getPayload();
        Long gatheringId = payload.getId();
        gatheringCreateRepository.createOrUpdate(gatheringId, TimeCalculatorUtils.calculateDurationToMidnight());
    }

    @Override
    public boolean supports(Event<GatheringCreatedEventPayload> event) {
        return EventType.GATHERING_CREATED == event.getType();
    }

    @Override
    public Long findGatheringId(Event<GatheringCreatedEventPayload> event) {
        return event.getPayload().getId();
    }
}
