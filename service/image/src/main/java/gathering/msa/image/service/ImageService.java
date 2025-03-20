package gathering.msa.image.service;

import dto.response.image.ImageUrlResponse;
import dto.response.image.SaveImageResponse;
import exception.image.NotFoundImageException;
import gathering.msa.image.entity.Image;
import gathering.msa.image.repository.ImageRepository;
import gathering.msa.image.s3.S3ImageDownloadService;
import gathering.msa.image.s3.S3ImageUploadService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import snowflake.Snowflake;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static util.ConstClass.*;


@Service
@RequiredArgsConstructor
@Transactional
public class ImageService {
    private final S3ImageDownloadService s3ImageDownloadService;
    private final S3ImageUploadService s3ImageUploadService;
    private final ImageRepository imageRepository;
    private final Snowflake snowflake = new Snowflake();
    @Value("${server.endpoint}")
    private String endpoint;

    public Resource image(String imageUrl) throws IOException {
        byte[] imageBytes = s3ImageDownloadService.getFileByteArrayFromS3(imageUrl);
        return new ByteArrayResource(imageBytes);
    }

    public SaveImageResponse saveImage(List<MultipartFile> files, Long boardId) throws IOException {

        List<Image> images = new ArrayList<>();
        for(MultipartFile file : files){
            if(!file.isEmpty()){
                String url = s3ImageUploadService.upload(file);
                Image image = Image.builder()
                        .id(snowflake.nextId())
                        .url(url)
                        .boardId(boardId)
                        .build();
                    images.add(image);
            }
        }
        imageRepository.saveAll(images);
        return SaveImageResponse.of(SUCCESS_CODE,SUCCESS_MESSAGE,toList(images));
    }

    public ImageUrlResponse url(List<Long> imageIds) {
        List<Image> images = imageRepository.findAllById(imageIds);
        List<String> urls = images.stream().map(
                image -> getUrl(image.getUrl())
        )
                .toList();
        return ImageUrlResponse.of(SUCCESS_CODE,SUCCESS_MESSAGE,urls);
    }

    private List<Long> toList(List<Image> images){
        return images.stream().map(i -> i.getId())
                .toList();
    }

    private String getUrl(String fileUrl){
        return endpoint+fileUrl;
    }

}
