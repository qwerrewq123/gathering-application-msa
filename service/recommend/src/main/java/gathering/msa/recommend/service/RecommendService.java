package gathering.msa.recommend.service;

import dto.response.gathering.GatheringPagingResponse;
import dto.response.gathering.RecommendGatheringResponse;
import event.Event;
import event.EventPayload;
import gathering.msa.recommend.repository.RecommendGatheringListRepository;
import gathering.msa.recommend.service.eventhandler.EventHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
@Service
@RequiredArgsConstructor
public class RecommendService {
    private final List<EventHandler> eventHandlers;
    private final RecommendScoreUpdater recommendScoreUpdater;
    private final RecommendGatheringListRepository recommendGatheringListRepository;
    public RecommendGatheringResponse readAll() {
        LocalDate today = LocalDate.now().minusDays(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String dateStr = today.format(formatter);
        return recommendGatheringListRepository.readAll(dateStr);
    }

    public void handleEvent(Event<EventPayload> event) {
        EventHandler<EventPayload> eventHandler = findEventHandler(event);
        if (eventHandler == null) {
            return;
        }
        recommendScoreUpdater.update(event,eventHandler);
    }

    private EventHandler<EventPayload> findEventHandler(Event<EventPayload> event) {
        return eventHandlers.stream()
                .filter(eventHandler -> eventHandler.supports(event))
                .findAny()
                .orElse(null);
    }
}
