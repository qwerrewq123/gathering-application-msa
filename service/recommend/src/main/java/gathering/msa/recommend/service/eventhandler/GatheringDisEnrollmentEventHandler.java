package gathering.msa.recommend.service.eventhandler;

import event.Event;
import event.EventType;
import event.payload.GatheringDisEnrollmentEventPayload;
import event.payload.MeetingDisAttendEventPayload;
import gathering.msa.recommend.repository.GatheringCountRepository;
import gathering.msa.recommend.utils.TimeCalculatorUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GatheringDisEnrollmentEventHandler implements EventHandler<GatheringDisEnrollmentEventPayload>{
    private final GatheringCountRepository gatheringCountRepository;
    @Override
    public void handle(Event<GatheringDisEnrollmentEventPayload> event) {
        GatheringDisEnrollmentEventPayload payload = event.getPayload();
        Long gatheringId = payload.getGatheringId();
        gatheringCountRepository.createOrUpdate(gatheringId, TimeCalculatorUtils.calculateDurationToMidnight());
    }

    @Override
    public boolean supports(Event<GatheringDisEnrollmentEventPayload> event) {
        return EventType.GATHERING_DIS_ENROLLMENT == event.getType();
    }

    @Override
    public Long findGatheringId(Event<GatheringDisEnrollmentEventPayload> event) {
        return event.getPayload().getGatheringId();
    }
}
