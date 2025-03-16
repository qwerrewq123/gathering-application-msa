package dto.response.gathering;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class GatheringPagingResponse {

    private String code;
    private String message;
    private List<GatheringsResponse> elements = new ArrayList<>();
    private boolean hasNext;


}
