package my.imageservice.image.controller;

import lombok.RequiredArgsConstructor;
import my.imageservice.dto.ImageListResponse;
import my.imageservice.dto.ImageResponse;
import my.imageservice.image.service.ImageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @PostMapping(value = "/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<ImageResponse> upload(@RequestParam("file") MultipartFile multipartFile){
        ImageResponse imageResponse = imageService.upload(multipartFile);
        if(imageResponse.getCode().equals("SU")){
            return new ResponseEntity<>(imageResponse, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(imageResponse, HttpStatus.BAD_REQUEST);
        }
    }



    @GetMapping("/image/{imageId}")
    ResponseEntity<ImageResponse> fetch(@PathVariable Long imageId){
        ImageResponse imageResponse = imageService.fetch(imageId);
        if(imageResponse.getCode().equals("SU")){
            return new ResponseEntity<>(imageResponse, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(imageResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/images")
    public ResponseEntity<ImageListResponse> fetchList(@RequestParam List<Long> ids){
        ImageListResponse imageListResponse = imageService.fetchList(ids);

        if(imageListResponse.getCode().equals("SU")){
            return new ResponseEntity<>(imageListResponse, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(imageListResponse, HttpStatus.BAD_REQUEST);

        }
    }
}
