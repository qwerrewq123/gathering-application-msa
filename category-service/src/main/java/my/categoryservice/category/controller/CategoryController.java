package my.categoryservice.category.controller;

import lombok.RequiredArgsConstructor;
import my.categoryservice.category.service.CategoryService;
import my.categoryservice.dto.CategoryListResponse;
import my.categoryservice.dto.CategoryResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/category")
    public ResponseEntity<CategoryResponse> fetchName(@RequestParam String categoryName){
        CategoryResponse categoryResponse = categoryService.fetch(categoryName);

        if(categoryResponse.getCode().equals("SU")){
            return new ResponseEntity<>(categoryResponse, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(categoryResponse, HttpStatus.BAD_REQUEST);

        }
    }


    @GetMapping("/category/{categoryId}")
    public ResponseEntity<CategoryResponse> fetchId(@PathVariable Long categoryId){
        CategoryResponse categoryResponse = categoryService.fetch(categoryId);

        if(categoryResponse.getCode().equals("SU")){
            return new ResponseEntity<>(categoryResponse, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(categoryResponse, HttpStatus.BAD_REQUEST);

        }
    }

    @GetMapping("/categorys")
    public ResponseEntity<CategoryListResponse> fetchList(@RequestParam List<Long> ids){
        CategoryListResponse categoryListResponse = categoryService.fetchList(ids);

        if(categoryListResponse.getCode().equals("SU")){
            return new ResponseEntity<>(categoryListResponse, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(categoryListResponse, HttpStatus.BAD_REQUEST);

        }
    }
}
