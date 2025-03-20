package gathering.msa.recommend.service.eventhandler;

import event.Event;
import event.EventType;
import event.payload.GatheringEnrollmentEventPayload;
import event.payload.MeetingDisAttendEventPayload;
import gathering.msa.recommend.repository.GatheringCountRepository;
import gathering.msa.recommend.utils.TimeCalculatorUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GatheringEnrollmentEventHandler implements EventHandler<GatheringEnrollmentEventPayload>{
    private final GatheringCountRepository gatheringCountRepository;
    @Override
    public void handle(Event<GatheringEnrollmentEventPayload> event) {
        GatheringEnrollmentEventPayload payload = event.getPayload();
        Long gatheringId = payload.getGatheringId();
        gatheringCountRepository.createOrUpdate(gatheringId, TimeCalculatorUtils.calculateDurationToMidnight());
    }

    @Override
    public boolean supports(Event<GatheringEnrollmentEventPayload> event) {
        return EventType.GATHERING_ENROLLMENT == event.getType();
    }

    @Override
    public Long findGatheringId(Event<GatheringEnrollmentEventPayload> event) {
        return event.getPayload().getGatheringId();
    }
}
