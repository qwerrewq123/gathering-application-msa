package gathering.msa.gathering.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import snowflake.Snowflake;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "gathering_count")
@AllArgsConstructor
@Builder
public class GatheringCount {
    @Id
    private Long id;
    @OneToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "gathering_id")
    private Gathering gathering;
    private int count;

    public static GatheringCount of(Snowflake snowflake,Gathering gathering, int count) {
        return GatheringCount.builder()
                .id(snowflake.nextId())
                .gathering(gathering)
                .count(count)
                .build();
    }
    public void chagneCount(int count) {
        this.count  = count;
    }
}
