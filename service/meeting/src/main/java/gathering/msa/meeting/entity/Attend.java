package gathering.msa.meeting.entity;

import dto.response.user.UserResponse;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import snowflake.Snowflake;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
public class Attend {

    @Id
    private Long id;
    @Column(name = "gathring_id")
    private Long gatheringId;
    private Boolean accepted;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meeting_id")
    private Meeting meeting;
    @Column(name = "user_id")
    private Long userId;
    private LocalDateTime date;
    public static Attend of(Snowflake snowflake,boolean accepted, Meeting meeting, UserResponse userResponse, LocalDateTime date) {
        return Attend.builder()
                .id(snowflake.nextId())
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

}

