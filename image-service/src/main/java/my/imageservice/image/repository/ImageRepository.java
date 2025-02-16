package my.imageservice.image.repository;

import my.imageservice.image.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image,Long> {
    @Query("select i from Image  i where i.id in :ids")
    List<Image> fetchList(List<Long> ids);
}
