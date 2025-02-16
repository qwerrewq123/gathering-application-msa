package my.gatheringservice.gathering.service;

import lombok.RequiredArgsConstructor;
import my.gatheringservice.gathering.Gathering;
import my.gatheringservice.gathering.GatheringCount;
import my.gatheringservice.gathering.repository.GatheringCountRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GatheringCountService {

    private final GatheringCountRepository gatheringCountRepository;

    public void addCount(Long gatheringId){



        gatheringCountRepository.addCount(gatheringId);
    }

    public GatheringCount makeCount(Gathering gathering){

        return gatheringCountRepository.save(
                GatheringCount.builder()
                        .count(1)
                        .gathering(gathering)
                        .build()
        );
    }
}
