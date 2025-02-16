package my.gatheringservice.enrollment;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import my.gatheringservice.gathering.Gathering;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "enrollment")
@AllArgsConstructor
@Builder
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Boolean accepted;
    private LocalDateTime date;

    @Column(name = "user_id")
    private Long enrolledBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gathering_id")
    private Gathering gathering;
}