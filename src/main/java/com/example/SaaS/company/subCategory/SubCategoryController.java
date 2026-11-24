package com.example.SaaS.company.subCategory;

import com.example.SaaS.common.dto.ApiResponse;
import com.example.SaaS.common.entity.SubCategory;
import com.example.SaaS.common.entity.SubCategoryTranslation;
import jakarta.validation.Valid;
import lombok.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/company/subCategory")
public class SubCategoryController {
    final SubCategoryService subCategoryService;
    final SubCategoryMapper  subCategoryMapper;
    @GetMapping
    public ResponseEntity<ApiResponse<Page<SubCategoryResourceDTO>>>  getAllSubCategories(

           @RequestParam(defaultValue = "en") String lang ,          @RequestParam(defaultValue = "0") int page,
           @RequestParam(defaultValue = "10") int size
    ){
        Pageable pageable = PageRequest.of(page, size);

        Page<SubCategoryResourceDTO> responce = subCategoryService.getAll(pageable).map(
                subCategory -> {
                    SubCategoryTranslation translation = subCategoryService.resolveTranslationByLocale(subCategory.getTranslations() ,"en");
                    return subCategoryMapper.toResponse(subCategory, translation);
                }
        );
        return ResponseEntity.ok(new ApiResponse<>(true , "subcategory retreived successfully " ,  responce));

    }
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SubCategoryResourceDTO>> getSubCategoryById(@PathVariable Long id , @RequestParam(defaultValue = "en")  String lang)
    {
        SubCategory subCat = subCategoryService.getById(id);
        SubCategoryTranslation transaltion  = subCategoryService.resolveTranslationByLocale(subCat.getTranslations() ,lang);
        return ResponseEntity.ok(new ApiResponse<>(true , "subcategory retrived succesfully" ,subCategoryMapper.toResponse(subCat, transaltion)  ));
    }
        @DeleteMapping("/{id}")
         public ResponseEntity<ApiResponse<SubCategoryResourceDTO>> deleteSubCategory(@PathVariable Long id){
        subCategoryService.delete(id);
        return ResponseEntity.ok(new ApiResponse<>(true , "subcategory deleted successfully" , null));
         }

         @PostMapping
    public  ResponseEntity<ApiResponse<SubCategoryResourceDTO>> addSubCategory(@Valid @ModelAttribute SubCategoryRequest request){

        SubCategory saved =subCategoryService.create(request);
        SubCategoryTranslation translation = subCategoryService.resolveTranslationByLocale(saved.getTranslations() ,"en");
        return ResponseEntity.ok(new ApiResponse<>(true ,"subcatgeory created successfully" , subCategoryMapper.toResponse(saved, translation)));

         }

         @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<SubCategoryResourceDTO>>  updateSubCategory(@PathVariable Long id, @Valid @ModelAttribute SubCategoryRequest request){
             SubCategory saved =subCategoryService.update(id , request);
             SubCategoryTranslation translation = subCategoryService.resolveTranslationByLocale(saved.getTranslations() , "en");
             return  ResponseEntity.ok(new ApiResponse<>(true , "update successfully" , subCategoryMapper.toResponse(saved, translation)));
         }
    }


