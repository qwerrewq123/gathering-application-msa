package my.categoryservice.category.service;

import lombok.RequiredArgsConstructor;
import my.categoryservice.category.Category;
import my.categoryservice.category.repository.CategoryRepository;
import my.categoryservice.dto.CategoryListElement;
import my.categoryservice.dto.CategoryListResponse;
import my.categoryservice.dto.CategoryResponse;
import my.categoryservice.exception.NotFoundCategoryException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static my.categoryservice.util.CategoryConst.*;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    public CategoryResponse fetch(String categoryName) {
        try {
            Category category = categoryRepository.findByName(categoryName)
                    .orElseThrow(() -> new NotFoundCategoryException("no Found Category"));

            return CategoryResponse.builder()
                    .code(successCode)
                    .message(successMessage)
                    .id(category.getId())
                    .name(category.getName())
                    .build();
        }catch (NotFoundCategoryException e){
            return CategoryResponse.builder()
                    .code(notFoundCode)
                    .message(notFoundMessage)
                    .build();
        }
    }


    public CategoryResponse fetch(Long categoryId) {
        try {
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new NotFoundCategoryException("not Found Category"));
            return CategoryResponse.builder()
                    .code(successCode)
                    .message(successMessage)
                    .id(category.getId())
                    .name(category.getName())
                    .build();
        }catch (NotFoundCategoryException e){
            return CategoryResponse.builder()
                    .code(notFoundCode)
                    .message(notFoundMessage)
                    .build();
        }
    }

    public CategoryListResponse fetchList(List<Long> categoryIdList) {

        List<Category> categories = categoryRepository.fetchList(categoryIdList);

        if(categories.size()>0){

            List<CategoryListElement> collect = categories.stream()
                    .map(c -> CategoryListElement.builder()
                            .id(c.getId())
                            .name(c.getName())
                            .build())
                    .collect(Collectors.toList());

            return CategoryListResponse.builder()
                    .code(successCode)
                    .message(successMessage)
                    .categoryLIstElements(collect)
                    .build();
        }else{
            return CategoryListResponse.builder()
                    .code(failCode)
                    .message(failMessage)
                    .build();
        }



    }
}
