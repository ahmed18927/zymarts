package com.example.SaaS.superAdmin.feature;

import com.example.SaaS.common.entity.Feature;
import com.example.SaaS.common.entity.FeatureTranslation;
import com.example.SaaS.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/admin/feature")
@RequiredArgsConstructor
public class FeatureController {
    private final FeatureService featureService;
    private final FeatureMapper  featureMapper;
    @PostMapping
    public ResponseEntity<ApiResponse<FeatureResourceDTO>> addFeature(@Valid @ModelAttribute FeatureRequest request) {
        Feature saved = featureService.createFeature(request);
        FeatureTranslation translation = featureService.resolveTranslationByLocale(saved.getTranslations() ,"en");
        return ResponseEntity.ok(new ApiResponse<FeatureResourceDTO>(true ,"added successfully" , featureMapper.toResponce(saved, translation)));

    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<FeatureResourceDTO>>> getFeature(@RequestParam(defaultValue = "en") String lang
            ,          @RequestParam(defaultValue = "0") int page,
                                                                            @RequestParam(defaultValue = "10") int size ) {
        Pageable pageable  = PageRequest.of(page,size);
        Page<FeatureResourceDTO> responce = featureService.getAllFeatures(pageable).map(
                feature -> {
                    FeatureTranslation translation = featureService.resolveTranslationByLocale(feature.getTranslations() ,lang);
                    return   featureMapper.toResponce(feature, translation);
                }
        );
        return  ResponseEntity.ok(new ApiResponse<>(true ,"retrived successfully" , responce));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<FeatureResourceDTO>> getFeatureById(@PathVariable Long id , @RequestParam(defaultValue = "en") String lang)  {

        Feature feature = featureService.getFeatureById(id);
        FeatureTranslation translation = featureService.resolveTranslationByLocale(feature.getTranslations() ,lang);
        return  ResponseEntity.ok(new ApiResponse<>(true ,"retrived successfully" , featureMapper.toResponce(feature, translation)));
    }
    @DeleteMapping("/{id}")
     public  ResponseEntity<ApiResponse<FeatureResourceDTO>> deleteFeature(@PathVariable Long id){
        featureService.delete(id);
        return  ResponseEntity.ok(new ApiResponse<>(true ,"deleted successfully" , null));
     }
 @PutMapping("/{id}")
     public ResponseEntity<ApiResponse<FeatureResourceDTO>> updateFeature(
             @PathVariable Long id,
             @Valid @ModelAttribute FeatureRequest request ) {
        Feature saved =  featureService.updateFeature(id, request);
        FeatureTranslation translation = featureService.resolveTranslationByLocale(saved.getTranslations() ,"en");
        return  ResponseEntity.ok(
                new ApiResponse<>(true ,"updated successfully" , featureMapper.toResponce(saved, translation))
        );
     }
}
