package dto.response.recommend;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class RecommendResponse {

    private String code;
    private String message;
//    List<GatheringResponse> gatherings;
//
//    public static RecommendResponse of(String code, String message, List<GatheringResponse> gatherings) {
//        return new RecommendResponse(code, message, gatherings);
//    }
}
