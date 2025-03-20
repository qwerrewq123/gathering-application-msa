package gathering.msa.recommend.service.eventhandler;

import event.Event;
import event.EventType;
import event.payload.GatheringViewEventPayload;
import event.payload.MeetingDisAttendEventPayload;
import gathering.msa.recommend.repository.GatheringMeetingCountRepository;
import gathering.msa.recommend.repository.GatheringViewRepository;
import gathering.msa.recommend.utils.TimeCalculatorUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GatheringViewEventHandler implements EventHandler<GatheringViewEventPayload>{
    private final GatheringViewRepository gatheringViewRepository;
    @Override
    public void handle(Event<GatheringViewEventPayload> event) {
        GatheringViewEventPayload payload = event.getPayload();
        Long gatheringId = payload.getId();
        gatheringViewRepository.createOrUpdate(gatheringId, TimeCalculatorUtils.calculateDurationToMidnight());
    }

    @Override
    public boolean supports(Event<GatheringViewEventPayload> event) {
        return EventType.GATHERING_VIEW == event.getType();
    }

    @Override
    public Long findGatheringId(Event<GatheringViewEventPayload> event) {
        return event.getPayload().getId();
    }
}
