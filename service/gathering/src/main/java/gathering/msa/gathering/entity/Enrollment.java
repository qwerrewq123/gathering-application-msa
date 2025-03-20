package gathering.msa.gathering.entity;

import dto.response.user.UserResponse;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import snowflake.Snowflake;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "enrollment")
@AllArgsConstructor
@Builder
public class Enrollment {
    @Id
    private Long id;
    private Boolean accepted;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gathering_id")
    private Gathering gathering;
    @Column(name = "user_id")
    private Long userId;
    private LocalDateTime date;

    public static Enrollment of(Snowflake snowflake,boolean accepted, Gathering gathering, UserResponse userResponse, LocalDateTime date) {
        return Enrollment.builder()
                .id(snowflake.nextId())
                .accepted(accepted)
                .gathering(gathering)
                .userId(userResponse.getId())
                .date(date)
                .build();
    }

}
