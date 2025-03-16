package gathering.msa.meeting.entity;

import dto.response.user.UserResponse;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
public class Attend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Boolean accepted;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meeting_id")
    private Meeting meeting;
    @Column(name = "user_id")
    private Long userId;
    private LocalDateTime date;
    public static Attend of(boolean accepted, Meeting meeting, UserResponse userResponse, LocalDateTime date) {
        return Attend.builder()
                .accepted(accepted)
                .meeting(meeting)
                .userId(userResponse.getId())
                .date(date)
                .build();
    }
    public void changeAccepted(boolean accepted) {
        this.accepted = accepted;
    }
    public void addMeeting(Meeting meeting){
        this.meeting = meeting;
    }
//    public static Attend of(Meeting meeting, User user){
//        return Attend.builder()
//                .meeting(meeting)
//                .date(LocalDateTime.now())
//                .accepted(true)
//                .attendBy(user)
//                .build();
//    }

}

