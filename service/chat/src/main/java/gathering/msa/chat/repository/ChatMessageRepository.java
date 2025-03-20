package gathering.msa.chat.repository;

import dto.response.chat.ChatMessageResponse;
import gathering.msa.chat.entity.ChatMessage;
import gathering.msa.chat.entity.ChatParticipant;
import gathering.msa.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByChatRoomAndChatParticipant(ChatRoom chatRoom, ChatParticipant chatParticipant);

    @Query("select new dto.response.chat." +
            "ChatMessageResponse(cm.chatRoom.id,cm.content,p.user.username,r.status) from ChatMessage cm " +
            "left join cm.chatParticipant p " +
            "left join ReadStatus r on r.chatMessage.id = cm.id and r.chatParticipant.id = :chatParticipantId " +
            "where cm.chatRoom.id = :roomId " +
            "order by case when r.status = true then 0 else 1 end")
    List<ChatMessageResponse> fetchMessages(Long roomId, Long chatParticipantId);
}
