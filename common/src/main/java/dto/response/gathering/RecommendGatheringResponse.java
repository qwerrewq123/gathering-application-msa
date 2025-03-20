package dto.response.gathering;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class RecommendGatheringResponse {

    private String code;
    private String message;
    private List<GatheringsResponse> elements = new ArrayList<>();

    public static RecommendGatheringResponse of(String code, String message,List<GatheringsResponse> elements) {
        return new RecommendGatheringResponse(code,message,elements);
    }
}
