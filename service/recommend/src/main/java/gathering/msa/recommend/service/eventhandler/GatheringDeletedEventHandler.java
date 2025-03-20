package gathering.msa.recommend.service.eventhandler;

import event.Event;
import event.EventType;
import event.payload.GatheringDeleteEventPayload;
import event.payload.MeetingDisAttendEventPayload;
import gathering.msa.recommend.repository.GatheringCreateRepository;
import gathering.msa.recommend.utils.TimeCalculatorUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GatheringDeletedEventHandler implements EventHandler<GatheringDeleteEventPayload>{
    private final GatheringCreateRepository gatheringCreateRepository;
    @Override
    public void handle(Event<GatheringDeleteEventPayload> event) {
        GatheringDeleteEventPayload payload = event.getPayload();
        Long gatheringId = payload.getId();
        gatheringCreateRepository.createOrUpdate(gatheringId, TimeCalculatorUtils.calculateDurationToMidnight());
    }

    @Override
    public boolean supports(Event<GatheringDeleteEventPayload> event) {
        return EventType.GATHERING_DELETED == event.getType();
    }

    @Override
    public Long findGatheringId(Event<GatheringDeleteEventPayload> event) {
        return event.getPayload().getId();
    }
}
