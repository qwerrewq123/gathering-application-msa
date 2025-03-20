package gathering.msa.recommend.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RecommendBackUpScheduler {

    //TODO : back up
    @Scheduled(cron ="0 0 */3 * * *")
    public void backUp(){

    }
}
