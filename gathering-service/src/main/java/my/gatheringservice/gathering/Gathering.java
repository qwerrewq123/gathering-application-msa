package my.gatheringservice.gathering;

import jakarta.persistence.*;
import lombok.*;
import my.gatheringservice.enrollment.Enrollment;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Setter
@Entity
@Table(name = "gathering")
@AllArgsConstructor
@Builder
public class Gathering {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content;
    private LocalDateTime registerDate;

    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "user_id")
    private Long createById;


    @Column(name = "image_id")
    private Long imageId;

    @OneToOne(mappedBy = "gathering",fetch = FetchType.LAZY,optional = false)
    private GatheringCount gatheringCount;

    @OneToMany(mappedBy = "gathering")
    List<Enrollment> enrollments = new ArrayList<>();
}
