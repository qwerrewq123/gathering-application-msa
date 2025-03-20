package gathering.msa.recommend.controller;

import dto.response.gathering.RecommendGatheringResponse;
import gathering.msa.recommend.service.RecommendService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class RecommendController {
    private final RecommendService recommendService;
    @GetMapping("/recommend")
    public RecommendGatheringResponse readAll(
    ) {
        return recommendService.readAll();
    }
}
