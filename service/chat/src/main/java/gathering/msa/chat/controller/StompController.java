package gathering.msa.chat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class StompController {

    private final KafkaProducerService kafkaProducerService;

    @MessageMapping("/{roomId}")
    public void sendMessage(@DestinationVariable Long roomId, ChatMessageRequest chatMessageRequest){
        kafkaProducerService.publishSendMessageEvent(EventType.Topic.GATHERING_CHAT, chatMessageRequest);
    }
}
