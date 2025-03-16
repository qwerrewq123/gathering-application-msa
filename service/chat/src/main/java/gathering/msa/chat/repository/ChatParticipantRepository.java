package gathering.msa.chat.repository;

import gathering.msa.chat.entity.ChatParticipant;
import gathering.msa.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ChatParticipantRepository extends JpaRepository<ChatParticipant, Long> {
    @Query("select p from ChatParticipant p left join p.chatRoom c where p.status =:status and p.userId=:userId and c.id=:chatRoomId")
    Optional<ChatParticipant> findByChatRoomAndUserAndStatus(Long chatRoomId, Long userId, boolean status);

    List<ChatParticipant> findAllByChatRoomAndStatus(ChatRoom chatRoom, boolean status);

}
