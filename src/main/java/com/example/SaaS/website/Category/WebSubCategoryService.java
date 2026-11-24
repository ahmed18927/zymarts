package com.example.SaaS.website.Category;

import com.example.SaaS.common.entity.SubCategory;
import com.example.SaaS.company.subCategory.SubCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class WebSubCategoryService {
    @Autowired
    private SubCategoryRepository subCategoryRepository;
     @Autowired
    private WebSubCategoryMapper subCategoryMapper;

     public Page<SubCategoryDto> findByCategoryId(Long categoryId , Pageable pageable , String lang) {
         return  subCategoryRepository.findByCategoryId(categoryId ,pageable)
                 .map(sc -> subCategoryMapper.toDto(sc, lang));

     }

}
