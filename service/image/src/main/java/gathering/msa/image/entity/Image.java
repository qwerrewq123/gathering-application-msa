package gathering.msa.image.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Table(name = "image")
@Entity
@AllArgsConstructor
@Builder
public class Image {
    @Id
    private Long id;
    private String url;
    @Column(name = "board_id")
    private Long boardId;
    @Column(name = "gathering_id")
    private Long gatheringId;
}
