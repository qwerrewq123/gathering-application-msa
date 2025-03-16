package dto.response.gathering;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TotalGatheringsResponse {
    private String code;
    private String message;
    private Map<String,CategoryTotalGatherings> map = new HashMap<>();
}
