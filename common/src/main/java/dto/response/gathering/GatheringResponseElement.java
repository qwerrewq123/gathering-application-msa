package dto.response.gathering;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GatheringResponseElement {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime registerDate;
    private String category;
    private String createdBy;
    private String image;
    private int count;

    public static GatheringResponseElement of(GatheringResponseQuery query) {
        return GatheringResponseElement.builder()
                .id(query.getId())
                .title(query.getTitle())
                .content(query.getContent())
                .registerDate(query.getRegisterDate())
                .category(query.getCategory())
                .count(query.getCount())
                .build();
    }
}
