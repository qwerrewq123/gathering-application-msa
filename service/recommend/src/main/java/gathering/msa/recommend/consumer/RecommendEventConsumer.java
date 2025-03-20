package gathering.msa.recommend.consumer;

import event.Event;
import event.EventPayload;
import event.EventType;
import gathering.msa.recommend.service.RecommendService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RecommendEventConsumer {
    private final RecommendService recommendService;

    @KafkaListener(topics = {
            EventType.Topic.MSA_GATHERING,
            EventType.Topic.MSA_GATHERING_ENROLLMENT,
            EventType.Topic.MSA_MEETING,
            EventType.Topic.MSA_MEETING_ATTEND,
            EventType.Topic.MSA_CHAT
    })
    public void listen(String message, Acknowledgment ack) {
        Event<EventPayload> event = Event.fromJson(message);
        if(event != null){
            recommendService.handleEvent(event);
        }
        ack.acknowledge();
    }
}
