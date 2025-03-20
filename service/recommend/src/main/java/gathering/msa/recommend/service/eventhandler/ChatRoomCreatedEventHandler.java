package gathering.msa.recommend.service.eventhandler;

import event.Event;
import event.EventType;
import event.payload.ChatRoomCreatedEventPayload;
import event.payload.MeetingDisAttendEventPayload;
import gathering.msa.recommend.repository.GatheringChatCreateRepository;
import gathering.msa.recommend.utils.TimeCalculatorUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatRoomCreatedEventHandler implements EventHandler<ChatRoomCreatedEventPayload>{
    private final GatheringChatCreateRepository gatheringChatCreateRepository;
    @Override
    public void handle(Event<ChatRoomCreatedEventPayload> event) {
        ChatRoomCreatedEventPayload payload = event.getPayload();
        Long gatheringId = payload.getGatheringId();
        gatheringChatCreateRepository.createOrUpdate(gatheringId, TimeCalculatorUtils.calculateDurationToMidnight());
    }

    @Override
    public boolean supports(Event<ChatRoomCreatedEventPayload> event) {
        return EventType.CHAT_ROOM_CREATED == event.getType();
    }

    @Override
    public Long findGatheringId(Event<ChatRoomCreatedEventPayload> event) {
        return event.getPayload().getGatheringId();
    }
}
