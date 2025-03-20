package gathering.msa.recommend.service;

import event.Event;
import event.EventPayload;
import gathering.msa.recommend.repository.RecommendGatheringListRepository;
import gathering.msa.recommend.service.eventhandler.EventHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class RecommendScoreUpdater {
    private final RecommendGatheringListRepository recommendGatheringListRepository;
    private final RecommendScoreCalculator recommendScoreCalculator;
    private static final long RECOMMEND_GATHERING_COUNT = 10;
    private static final Duration RECOMMEND_GATHERING_TTL = Duration.ofDays(10);
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

    public void update(Event<EventPayload> event, EventHandler<EventPayload> eventHandler) {
        Long gatheringId = eventHandler.findGatheringId(event);


        eventHandler.handle(event);

        long score = recommendScoreCalculator.calculate(gatheringId);
        recommendGatheringListRepository.add(
                gatheringId,
                LocalDateTime.now(),
                score,
                RECOMMEND_GATHERING_COUNT,
                RECOMMEND_GATHERING_TTL
        );
    }
}
