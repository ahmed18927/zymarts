package com.example.SaaS.website.main;

import com.example.SaaS.common.entity.*;
import com.example.SaaS.company.banner.BannerRepository;
import com.example.SaaS.company.category.CategoryRepository;
import com.example.SaaS.company.product.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HomeService {
    @Autowired private BannerRepository bannerRepo;
    @Autowired private CategoryRepository categoryRepo;
    @Autowired private ProductRepository productRepo;
//    @Autowired private SettingService settingService;

    public HomeResponse getHomeData(String lang) {

        List<ContentBlock<?>> content = new ArrayList<>();

        // BANNERS
        List<Banner> banners = bannerRepo.findTop6ByOrderByCreatedAtDesc();
        boolean hasMore = banners.size() > 5;

        List<BannerDto> bannerDtos = banners.stream()
                .limit(5)
                .map(this::mapBanner)
                .toList();

        content.add(new ContentBlock<>("banners", bannerDtos, hasMore));

        // CATEGORIES
        List<CategoryDto> categories = categoryRepo.findTop10ByOrderByIdDesc()
                .stream()
                .map(cat -> mapCategory(cat, lang))
                .toList();

        content.add(new ContentBlock<>("categories", categories));

        // PRODUCTS
        List<ProductDto> products = productRepo.findTop10ByIsActiveTrue()
                .stream().map(p-> mapProduct(p, lang)).toList();


        content.add(new ContentBlock<>("popular_products", products));

        // SETTINGS
//        SettingsDto settings = new SettingsDto();
//        settings.setEmail(settingService.get("company_email"));
//        settings.setPhone(settingService.get("company_phone"));
//        settings.setFacebook(settingService.get("facebook"));
//        settings.setInstagram(settingService.get("instagram"));

//        content.add(new ContentBlock<>("settings", settings));

        HomeResponse response = new HomeResponse();
        response.setContent(content);

        return response;
    }

    private BannerDto mapBanner(Banner b) {
        BannerDto dto = new BannerDto();
        dto.setId(b.getId());
        dto.setImage(b.getImage());
        return dto;
    }
    private CategoryDto mapCategory(Category category, String lang) {

        CategoryDto dto = new CategoryDto();
        dto.setId(category.getId());
        dto.setName(getCategoryName(category, lang));

        return dto;
    }

    private ProductDto mapProduct(Product product, String lang) {

        ProductDto dto = new ProductDto();
        dto.setId(product.getId());
        dto.setName(getProductName(product, lang));

        return dto;
    }

    private String getCategoryName(Category category, String lang) {

        return category.getTranslations().stream()
                .filter(t -> t.getLocale().equalsIgnoreCase(lang))
                .map(CategoryTransaction::getName)
                .findFirst()
                .orElseGet(() ->
                        category.getTranslations().stream()
                                .filter(t -> t.getLocale().equalsIgnoreCase("en"))
                                .map(CategoryTransaction::getName)
                                .findFirst()
                                .orElse("")
                );
    }
    private String getProductName(Product product, String lang) {

        return product.getTranslations().stream()
                .filter(t -> t.getLangCode().equalsIgnoreCase(lang))
                .map(ProductTranslation::getName)
                .findFirst()
                .orElseGet(() ->
                        product.getTranslations().stream()
                                .filter(t -> t.getLangCode().equalsIgnoreCase("en"))
                                .map(ProductTranslation::getName)
                                .findFirst()
                                .orElse("")
                );
    }
}
