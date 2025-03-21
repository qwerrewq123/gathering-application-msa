package gathering.msa.gathering.service;

import gathering.msa.gathering.repository.GatheringViewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class GatheringViewService {

    private final GatheringViewRepository gatheringViewRepository;
    public int fetchCount(Long gatheringId) {
        return gatheringViewRepository.fetchCount(gatheringId, Duration.ofMinutes(30L));
    }
}
