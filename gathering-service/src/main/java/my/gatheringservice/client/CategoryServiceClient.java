package my.gatheringservice.client;

import my.gatheringservice.dto.response.category.CategoryListResponse;
import my.gatheringservice.dto.response.category.CategoryResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name ="category-service")
public interface CategoryServiceClient {


    @GetMapping(value = "/category")
    CategoryResponse fetchName(@RequestParam String categoryName,@RequestHeader("Authorization") String token);


    @GetMapping("/category/{categoryId}")
    CategoryResponse fetchId(@PathVariable Long categoryId,@RequestHeader("Authorization") String token);

    @GetMapping("/categorys")
    CategoryListResponse fetchList(@RequestParam List<Long> categoryIdList,@RequestHeader("Authorization") String token);
}

