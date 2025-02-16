package my.gatheringservice.dto.response.gathering;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import my.gatheringservice.dto.response.category.CategoryResponse;
import my.gatheringservice.dto.response.image.ImageResponse;
import my.gatheringservice.dto.response.user.UserResponse;
import my.gatheringservice.gathering.Gathering;
import my.gatheringservice.gathering.GatheringCount;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;

import static my.gatheringservice.util.GatheringConst.successCode;
import static my.gatheringservice.util.GatheringConst.successMessage;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
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

    public static GatheringResponse toGatheringResponse(Gathering gathering, GatheringCount gatheringCount, CategoryResponse categoryResponse,
                                                        ImageResponse imageResponse, UserResponse userResponse) throws IOException {
        return GatheringResponse.builder()
                .code(successCode)
                .message(successMessage)
                .title(gathering.getTitle())
                .content(gathering.getContent())
                .registerDate(gathering.getRegisterDate())
                .category(categoryResponse.getName())
                .createdBy(userResponse.getUsername())
                .image(encodeFileToBase64(imageResponse.getUrl()))
                .count(gatheringCount.getCount())
                .participatedBy(null)
                .build();
    }

    public static String encodeFileToBase64(String filePath) throws IOException {
        File file = new File(filePath);
        byte[] fileBytes  = Files.readAllBytes(file.toPath());
        return Base64.getEncoder().encodeToString(fileBytes);

    }
}
