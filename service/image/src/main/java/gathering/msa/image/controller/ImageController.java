package gathering.msa.image.controller;

import dto.response.image.ImageUrlResponse;
import dto.response.image.SaveImageResponse;
import gathering.msa.image.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;

    @GetMapping("/image/{imageUrl}")
    public ResponseEntity<Resource> image(@PathVariable String imageUrl) throws IOException {
        Resource resource = imageService.image(imageUrl);
        return new ResponseEntity<>(resource, HttpStatus.OK);
    }

    @GetMapping("/image/url")
    public ResponseEntity<ImageUrlResponse> url(@RequestParam List<Long> imageIds){
        ImageUrlResponse imageUrlResponse = imageService.url(imageIds);
        return new ResponseEntity<>(imageUrlResponse, HttpStatus.OK);
    }

    @PostMapping("/image")
    public ResponseEntity<SaveImageResponse> saveImage(@RequestPart List<MultipartFile> files, @RequestParam(required = false) Long boardId) throws IOException {
        SaveImageResponse saveImageResponse = imageService.saveImage(files,boardId);
        return ResponseEntity.status(HttpStatus.CREATED).body(saveImageResponse);
    }

}

