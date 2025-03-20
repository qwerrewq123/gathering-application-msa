package gathering.msa.gathering.entity;

import dto.request.gathering.AddGatheringRequest;
import dto.request.gathering.UpdateGatheringRequest;
import dto.response.category.CategoryResponse;
import dto.response.image.SaveImageResponse;
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
@Table(name = "gathering")
@AllArgsConstructor
@Builder
public class Gathering {

    @Id
    private Long id;
    private String title;
    private String content;
    private LocalDateTime registerDate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "image_id")
    private Long imageId;


    public static Gathering of(Snowflake snowflake,AddGatheringRequest addGatheringRequest, UserResponse userResponse, Category category, SaveImageResponse saveImageResponse){
        return Gathering.builder()
                .id(snowflake.nextId())
                .title(addGatheringRequest.getTitle())
                .content(addGatheringRequest.getContent())
                .userId(userResponse.getId())
                .category(category)
                .registerDate(LocalDateTime.now())
                .imageId(saveImageResponse.getIds().getFirst())
                .build();
    }
    public void changeGathering(UpdateGatheringRequest updateGatheringRequest, SaveImageResponse saveImageResponse, Category category){
        this.imageId = saveImageResponse.getIds().getFirst();
        this.category = category;
        this.title = updateGatheringRequest.getTitle();
        this.content = updateGatheringRequest.getContent();
        this.registerDate = LocalDateTime.now();
    }
}
