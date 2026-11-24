package com.example.SaaS.website.Category;

import com.example.SaaS.company.category.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class WebCategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private WebCategoryMapper categoryMapper;

    public Page<CategoryDto> getAllCategories(Pageable pageable, String lang) {

        return categoryRepository.findAll(pageable)
                .map(c -> categoryMapper.toDto(c, lang));
    }
}
