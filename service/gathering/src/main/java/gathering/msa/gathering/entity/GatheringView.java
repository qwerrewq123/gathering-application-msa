package gathering.msa.gathering.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "gathering_view")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GatheringView {
    @Id
    private Long id;
    @Column(name = "gathering_id")
    private Long gatheringId;
    private int count;
}
