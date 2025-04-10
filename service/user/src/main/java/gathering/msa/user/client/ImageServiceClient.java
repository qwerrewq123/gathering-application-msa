package gathering.msa.user.client;

import dto.response.image.ImageUrlResponse;
import dto.response.image.SaveImageResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@FeignClient(name ="image-service")
public interface ImageServiceClient {
    @PostMapping(value = "/image")
    SaveImageResponse saveImage(@RequestPart List<MultipartFile> files, @RequestParam(required = false) Long boardId);
    @PostMapping(value = "/image/{imageId}")
    ImageUrlResponse url(@PathVariable Long imageId);

}
