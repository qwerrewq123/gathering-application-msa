package my.meetingservice.client;

import my.meetingservice.dto.response.user.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user-service")
public interface UserServiceClient {

    @GetMapping(value = "/user")
    UserResponse fetchUsername(@RequestParam String username, @RequestHeader("Authorization") String token);

    @GetMapping(value = "/user/{userId}")
    UserResponse fetchUserId(@PathVariable Long userId, @RequestHeader("Authorization") String token);


}
