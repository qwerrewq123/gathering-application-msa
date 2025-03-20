package gathering.msa.recommend.service.eventhandler;

import event.Event;
import event.EventType;
import event.payload.ChatMessageCreatedEventPayload;
import event.payload.MeetingDisAttendEventPayload;
import gathering.msa.recommend.repository.GatheringChatSendCreateRepository;
import gathering.msa.recommend.utils.TimeCalculatorUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatMessageCreatedEventHandler implements EventHandler<ChatMessageCreatedEventPayload>{
    private final GatheringChatSendCreateRepository gatheringChatSendCreateRepository;
    @Override
    public void handle(Event<ChatMessageCreatedEventPayload> event) {
        ChatMessageCreatedEventPayload payload = event.getPayload();
        Long gatheringId = payload.getGatheringId();
        gatheringChatSendCreateRepository.createOrUpdate(gatheringId, TimeCalculatorUtils.calculateDurationToMidnight());
    }

    @Override
    public boolean supports(Event<ChatMessageCreatedEventPayload> event) {
        return EventType.CHAT_MESSAGE_CREATED == event.getType();
    }

    @Override
    public Long findGatheringId(Event<ChatMessageCreatedEventPayload> event) {
        return event.getPayload().getGatheringId();
    }
}
