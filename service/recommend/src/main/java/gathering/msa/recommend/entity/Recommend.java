package gathering.msa.recommend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "recommend")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Recommend {
    @Id
    private Long id;
    @Column(name = "gathering_id")
    private Long gatheringId;
    private int score;
}
