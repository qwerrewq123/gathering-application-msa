package my.gatheringservice.dto.request;

import lombok.Builder;
import lombok.Data;
import my.gatheringservice.dto.response.category.CategoryResponse;
import my.gatheringservice.dto.response.image.ImageResponse;
import my.gatheringservice.dto.response.user.UserResponse;
import my.gatheringservice.gathering.Gathering;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Data
@Builder
public class AddGatheringRequest {

    private String title;
    private String content;
    private String category;

    public static Gathering toGathering(AddGatheringRequest addGatheringRequest, UserResponse userResponse
            , ImageResponse imageResponse, CategoryResponse categoryResponse){

        return Gathering.builder()
                .title(addGatheringRequest.getTitle())
                .content(addGatheringRequest.getContent())
                .registerDate(LocalDateTime.now())
                .categoryId(categoryResponse.getId())
                .createById(userResponse.getId())
                .imageId(imageResponse.getId())
                .enrollments(new ArrayList<>())
                .build();

    }
}
