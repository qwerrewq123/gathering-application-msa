package my.meetingservice.client;

import my.meetingservice.dto.response.gathering.GatheringResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "gathering-service")
public interface GatheringServiceClient {

    @GetMapping("/gathering/{gatheringId}")
    GatheringResponse gatheringDetail(@PathVariable Long gatheringId, @RequestHeader("Authorization") String token);


}
