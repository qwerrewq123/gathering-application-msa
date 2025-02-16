package my.meetingservice.dto.response.gathering;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class GatheringResponse {

    private String code;
    private String message;
    private String title;
    private String content;
    private LocalDateTime registerDate;
    private String category;
    private String createdBy;
    private List<String> participatedBy;
    private String image;
    private int count;

}
