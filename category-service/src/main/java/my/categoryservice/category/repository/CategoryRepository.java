package my.categoryservice.category.repository;

import my.categoryservice.category.Category;
import my.categoryservice.dto.CategoryResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category,Long> {

    Optional<Category> findByName(String categoryName);

    @Query("select c from Category  c where c.id in :categoryIdList")
    List<Category> fetchList(List<Long> categoryIdList);
}
