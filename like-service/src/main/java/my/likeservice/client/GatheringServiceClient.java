package my.likeservice.client;

import my.likeservice.dto.response.GatheringResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.io.IOException;

@FeignClient(name = "user-service")
public interface GatheringServiceClient {

    @GetMapping("/gathering/{gatheringId}")
    GatheringResponse gatheringDetail(@PathVariable Long gatheringId, @RequestHeader("Authorization") String token);


}
