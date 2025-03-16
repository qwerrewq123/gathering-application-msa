package dto.response.gathering;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryTotalGatherings {
    private List<TotalGatheringsElement> totalGatherings = new ArrayList<>();
    boolean hasNext;

}
