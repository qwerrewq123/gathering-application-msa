package my.gatheringservice.dto.request;

import lombok.Builder;
import lombok.Data;
import my.gatheringservice.dto.response.category.CategoryResponse;
import my.gatheringservice.dto.response.image.ImageResponse;
import my.gatheringservice.gathering.Gathering;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

@Data
@Builder
public class UpdateGatheringRequest {

    private String title;
    private String content;
    private String category;


    public static void updateGathering(Gathering gathering,
                                       UpdateGatheringRequest updateGatheringRequest, ImageResponse imageResponse,
                                       CategoryResponse categoryResponse){

        gathering.setImageId(imageResponse.getId());
        gathering.setCategoryId(categoryResponse.getId());
        gathering.setContent(updateGatheringRequest.getContent());
        gathering.setTitle(updateGatheringRequest.getTitle());
        gathering.setRegisterDate(LocalDateTime.now());
    }
}
