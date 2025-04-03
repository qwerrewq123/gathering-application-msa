package gathering.msa.recommend.scheduler;

import gathering.msa.recommend.entity.Recommend;
import gathering.msa.recommend.repository.RecommendBackUpRepository;
import gathering.msa.recommend.repository.RecommendGatheringListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class RecommendBackUpScheduler {

    private final RecommendBackUpRepository recommendBackUpRepository;
    private final RecommendGatheringListRepository recommendGatheringListRepository;
    @Scheduled(cron ="0 0 */3 * * *")
    public void backUp(){
        recommendBackUpRepository.deleteAll();
        Set<ZSetOperations.TypedTuple<String>> all = recommendGatheringListRepository.findAll(LocalDateTime.now());
        List<Recommend> recommends = new ArrayList<>();
        for (ZSetOperations.TypedTuple<String> stringTypedTuple : all) {
            Long gatheringId = Long.parseLong(stringTypedTuple.getValue());
            int score = Integer.parseInt(stringTypedTuple.getScore().toString());
            recommends.add(Recommend.of(gatheringId, score));
        }
        recommendBackUpRepository.saveAll(recommends);
    }
}
