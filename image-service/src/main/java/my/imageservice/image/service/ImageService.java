package my.imageservice.image.service;

import lombok.RequiredArgsConstructor;
import my.imageservice.dto.ImageListElement;
import my.imageservice.dto.ImageListResponse;
import my.imageservice.dto.ImageResponse;
import my.imageservice.exception.NotFoundImageException;
import my.imageservice.image.Image;
import my.imageservice.image.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static my.imageservice.util.ImageConst.*;

@RequiredArgsConstructor
@Service
public class ImageService {

    private final ImageRepository imageRepository;

    @Value("${file.dir}")
    private String fileDir;


    public ImageResponse upload(MultipartFile file) {

        try {
            Image image = uploadImage(file);

            return ImageResponse.builder()
                            .code(successCode)
                            .message(successMessage)
                            .id(image.getId())
                            .url(image.getUrl())
                            .build();

        }catch (IOException e){

            return ImageResponse.builder()
                    .code(failUploadCode)
                    .message(failUploadMessage)
                    .build();
        }


    }

    private Image uploadImage(MultipartFile file) throws IOException {

        if(!file.isEmpty()){
            String[] split = file.getOriginalFilename().split("\\.");
            String fullPath = fileDir+"/" + UUID.randomUUID()+"."+split[1];
            file.transferTo(new File(fullPath));
            Image image = Image.builder()
                    .url(fullPath)
                    .build();
            imageRepository.save(image);
            return image;

        }
        return null;
    }


    public ImageResponse fetch(Long imageId) {

        try {
            Image image = imageRepository.findById(imageId).orElseThrow(() -> new NotFoundImageException("Not Found Image!!"));
            return ImageResponse.builder()
                    .code(successCode)
                    .message(successMessage)
                    .id(image.getId())
                    .url(image.getUrl())
                    .build();


        }catch (NotFoundImageException e){
            return ImageResponse.builder()
                    .code(notFoundCode)
                    .message(notFoundMessage)
                    .build();
        }

    }

    public ImageListResponse fetchList(List<Long> ids) {

        List<Image> images = imageRepository.fetchList(ids);

        if(images.size()>0){

            List<ImageListElement> collect = images.stream()
                    .map(i -> ImageListElement.builder()
                            .id(i.getId())
                            .url(i.getUrl())
                            .build())
                    .collect(Collectors.toList());

            return ImageListResponse.builder()
                    .code(successCode)
                    .message(successMessage)
                    .categoryLIstElements(collect)
                    .build();
        }else{
            return ImageListResponse.builder()
                    .code(failCode)
                    .message(failMessage)
                    .build();
        }
    }
}
