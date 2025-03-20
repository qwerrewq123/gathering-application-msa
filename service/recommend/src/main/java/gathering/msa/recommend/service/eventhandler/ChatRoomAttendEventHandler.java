package gathering.msa.recommend.service.eventhandler;

import event.Event;
import event.EventType;
import event.payload.ChatRoomAttendEventPayload;
import event.payload.MeetingDisAttendEventPayload;
import gathering.msa.recommend.repository.GatheringChatAttendCreateRepository;
import gathering.msa.recommend.utils.TimeCalculatorUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatRoomAttendEventHandler implements EventHandler<ChatRoomAttendEventPayload>{
    private final GatheringChatAttendCreateRepository gatheringChatAttendCreateRepository;
    @Override
    public void handle(Event<ChatRoomAttendEventPayload> event) {
        ChatRoomAttendEventPayload payload = event.getPayload();
        Long gatheringId = payload.getGatheringId();
        gatheringChatAttendCreateRepository.createOrUpdate(gatheringId, TimeCalculatorUtils.calculateDurationToMidnight());
    }

    @Override
    public boolean supports(Event<ChatRoomAttendEventPayload> event) {
        return EventType.CHAT_ROOM_ATTEND == event.getType();
    }

    @Override
    public Long findGatheringId(Event<ChatRoomAttendEventPayload> event) {
        return event.getPayload().getGatheringId();
    }
}
