package com.example.SaaS.company.category;

import com.example.SaaS.common.dto.ApiResponse;
import com.example.SaaS.common.entity.Category;
import com.example.SaaS.common.entity.CategoryTransaction;
import jakarta.validation.Valid;
import lombok.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/company/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
public ResponseEntity<ApiResponse<CategoryResourceDTO>> addCategory(
        @Valid  @ModelAttribute CategoryRequest request){

    Category saved = categoryService.create(request);
        CategoryTransaction translation =
            categoryService.resolveTranslationByLocale(saved.getTranslations() ,"en");
    return ResponseEntity.ok(
            new ApiResponse<CategoryResourceDTO>(
                    true , "catgeory created successfully", categoryMapper.toResponse(saved , translation)
            )
    );

}
@GetMapping
public  ResponseEntity<ApiResponse<Page<CategoryResourceDTO>>> getAllCategories (@RequestParam(defaultValue = "en") String lang ,
                                                                                 @RequestParam(defaultValue = "0") int page , @RequestParam(defaultValue = "10") int size

){
    Pageable pageable = PageRequest.of(page, size);
    Page<CategoryResourceDTO> responce = categoryService.getAll(pageable).map(
            category -> {
                CategoryTransaction translation = categoryService.resolveTranslationByLocale(category.getTranslations() , "en");
                return categoryMapper.toResponse(category, translation);
            }
    );
    return ResponseEntity.ok(new ApiResponse<>(true , "catgeories retrived successfully ", responce));
}

@GetMapping("/{id}")
public ResponseEntity<ApiResponse<CategoryResourceDTO>> getById(
        @PathVariable Long id ,
        @RequestParam(defaultValue = "en") String lang
){
    Category cat = categoryService.getById(id);
    CategoryTransaction translation = categoryService.resolveTranslationByLocale(cat.getTranslations() , lang);
    return ResponseEntity.ok(
            new ApiResponse<>(true , "category retrived successfully" , categoryMapper.toResponse(cat ,translation))
    );
}
@PutMapping("/{id}")
public ResponseEntity<ApiResponse<CategoryResourceDTO>> updateCategory(
        @PathVariable Long id ,
        @Valid @ModelAttribute CategoryRequest request
){
//    Category existing = categoryService.getById(id);
//    categoryService.upsertTranslation(existing ,"en",
//            request.getNameEn() , request.getDescriptionEn());
//    categoryService.upsertTranslation(existing ,"ar",
//            request.getNameAr() , request.getDescriptionAr());
    Category saved = categoryService.updateCategory(id ,request);
    CategoryTransaction translation =
            categoryService.resolveTranslationByLocale(saved.getTranslations() , "en");
    return  ResponseEntity.ok(new ApiResponse<>(true , "updated successfully" , categoryMapper.toResponse(saved , translation)));
}
@DeleteMapping("/{id}")
public ResponseEntity<ApiResponse<CategoryResourceDTO>> deleteCategory(@PathVariable Long id){
    categoryService.delete(id);
    return ResponseEntity.ok(new ApiResponse<>(true ,"deleted successfully" , null));
}
}
