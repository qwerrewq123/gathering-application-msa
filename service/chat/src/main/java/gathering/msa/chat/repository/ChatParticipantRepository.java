package gathering.msa.chat.repository;

import gathering.msa.chat.entity.ChatParticipant;
import gathering.msa.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ChatParticipantRepository extends JpaRepository<ChatParticipant, Long> {

    @Query("select cp from ChatParticipant cp where cp.chatRoom =:chatRoom and cp.userId = :userId and cp.status = :status")
    Optional<ChatParticipant> findByChatRoomAndUserAndStatus(ChatRoom chatRoom, Long userId, boolean status);

    List<ChatParticipant> findAllByChatRoomAndStatus(ChatRoom chatRoom, boolean status);

}
