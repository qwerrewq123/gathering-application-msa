package my.meetingservice.attend;

import jakarta.persistence.*;
import lombok.*;
import my.meetingservice.meeting.Meeting;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
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
    private Long attendById;

    private LocalDateTime date;

}
