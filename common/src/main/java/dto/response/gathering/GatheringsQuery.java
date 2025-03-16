package dto.response.gathering;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class GatheringsQuery {

    private Long id;
    private String title;
    private String content;
    private LocalDateTime registerDate;
    private String category;
    private Long createdById;
    private Long imageId;
    private int count;
}
