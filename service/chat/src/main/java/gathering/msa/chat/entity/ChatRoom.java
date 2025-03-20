package gathering.msa.chat.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Table(name = "chat_room")
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(name = "user_id")
    private Long userId;
    private int count;
    public void changeCount(int count){
        this.count = count;
    }
    public static ChatRoom of(String name,Long userId){
        return ChatRoom.builder()
                .name(name)
                .userId(userId)
                .count(1)
                .build();
    }

}
