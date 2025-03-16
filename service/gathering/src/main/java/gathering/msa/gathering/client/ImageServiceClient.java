package gathering.msa.gathering.client;

import dto.response.image.ImageUrlResponse;
import dto.response.image.SaveImageResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@FeignClient(name ="image-service")
public interface ImageServiceClient {
    @PostMapping(value = "/image")
    SaveImageResponse saveImage(@RequestPart List<MultipartFile> files, @RequestParam(required = false) Long boardId);
    @GetMapping("/image/url")
    ImageUrlResponse url(@RequestParam List<Long> imageIds);

}
