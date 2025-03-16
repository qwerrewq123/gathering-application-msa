package gathering.msa.chat.client;

import dto.response.user.UserResponse;
import dto.response.user.UserResponses;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name ="user-service")
public interface UserServiceClient {
    @GetMapping(value = "/user/username")
    UserResponse fetchUserByUsername(@RequestParam("username") String username);
    @GetMapping("/user/id")
    UserResponses fetchUserByIds(@RequestParam("id") List<Long> ids);

}
