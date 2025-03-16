package gathering.msa.chat.repository;

import dto.response.chat.ChatMyRoomResponse;
import dto.response.chat.ChatRoomResponse;
import gathering.msa.chat.entity.ChatRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    @Query("select new dto.response.chat." +
            "ChatRoomResponse(c.id,c.name,c.count,c.userId," +
            "case when cp.id is not null then true else false end) from ChatRoom c " +
            "left join ChatParticipant cp on cp.chatRoom.id == c.id " +
            "where c.userId = :userId " +
            "order by case when cp.id is not null then 0 else 1 end, c.id asc")
    Page<ChatRoomResponse> fetchChatRooms(Pageable pageable, Long userId);

    @Query("select new dto.response.chat." +
            "ChatMyRoomResponse(c.id,c.name,c.count,c.userId,true,count(r.id)) " +
            "from ChatParticipant p join p.chatRoom c " +
            "left join ReadStatus r on r.chatParticipant.id = p.id and r.status=false " +
            "where p.id = :userId " +
            "group by c.id")
    Page<ChatMyRoomResponse> fetchMyChatRooms(Pageable pageable, Long userId);
}
