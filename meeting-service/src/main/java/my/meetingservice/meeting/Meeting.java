package my.meetingservice.meeting;

import jakarta.persistence.*;
import lombok.*;
import my.meetingservice.attend.Attend;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
public class Meeting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;

    private LocalDateTime boardDate;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String content;

    @Column(name = "user_id")
    private Long createdById;

    @Column(name = "gathering_id")
    private Long gatheringId;

    @OneToMany(mappedBy = "meeting")
    private List<Attend> attends = new ArrayList<>();

}
