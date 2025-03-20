package gathering.msa.chat.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import spring.myproject.entity.user.User;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Table(name = "chat_participant")
public class ChatParticipant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "user_id")
    private Long userId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id")
    ChatRoom chatRoom;
    boolean status;

    public void changeStatus(boolean status) {
        this.status = status;
    }

    public static ChatParticipant of(ChatRoom chatRoom,Long userId){
        return ChatParticipant.builder()
                .chatRoom(chatRoom)
                .userId(userId)
                .status(false)
                .build();
    }


}
