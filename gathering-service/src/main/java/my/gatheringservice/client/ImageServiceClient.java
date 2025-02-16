package my.gatheringservice.client;

import my.gatheringservice.dto.response.image.ImageListResponse;
import my.gatheringservice.dto.response.image.ImageResponse;
import my.gatheringservice.dto.response.user.UserListResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@FeignClient(name = "image-service")
public interface ImageServiceClient {


    @PostMapping(value = "/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ImageResponse upload(@RequestPart("file") MultipartFile file,@RequestHeader("Authorization") String token);

    @GetMapping("/image/{imageId}")
    ImageResponse fetch(@PathVariable Long imageId,@RequestHeader("Authorization") String token);

    @GetMapping("/images")
    ImageListResponse fetchList(@RequestParam List<Long> ids, @RequestHeader("Authorization") String token);
}

