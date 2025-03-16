package gathering.msa.meeting.client;

import dto.response.image.ImageUrlResponse;
import dto.response.image.SaveImageResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@FeignClient(name ="image-service")
public interface ImageServiceClient {
    @PostMapping(value = "/image")
    SaveImageResponse saveImage(@RequestPart List<MultipartFile> files, @RequestParam(required = false) Long boardId);
    @GetMapping("/image/url")
    ImageUrlResponse url(@RequestParam List<Long> imageIds);

}
