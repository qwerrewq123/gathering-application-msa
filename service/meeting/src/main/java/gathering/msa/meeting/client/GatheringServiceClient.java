package gathering.msa.meeting.client;

import dto.response.gathering.GatheringResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name ="gathering-service")
public interface GatheringServiceClient {
    @GetMapping("/feign/gathering/{gatheringId}")
    GatheringResponse fetchFeignGathering(@PathVariable Long gatheringId);

}