package com.example.SaaS.landPage;

import com.example.SaaS.common.response.ApiResponse;
import com.example.SaaS.superAdmin.feature.FeatureService;
import com.example.SaaS.superAdmin.setting.SettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.SaaS.superAdmin.packagee.PackageService;
import com.example.SaaS.superAdmin.packagee.PackageMapper;
import com.example.SaaS.superAdmin.packagee.PackageResponseDTO;
import com.example.SaaS.common.entity.PackageTranslation;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/landPage")
@RequiredArgsConstructor
public class PublicController {
    private final SettingService service;
    private final FeatureService featureService;
    private final PackageService packageService;
    private final PackageMapper packageMapper;


//    @GetMapping("/about-us")
//    public ResponseEntity<ApiResponse<String>> getAboutUs() {
//
//        String value = service.getPublicSettingByKey("about_us");
//
//        return ResponseEntity.ok(
//                new ApiResponse<>(
//                        true,
//                        "About us fetched successfully",
//                        value
//                )
//        );
//    }
//
//    @GetMapping("/terms")
//    public ResponseEntity<ApiResponse<String>> getTerms() {
//
//        String value = service.getPublicSettingByKey("terms");
//
//        return ResponseEntity.ok(
//                new ApiResponse<>(
//                        true,
//                        "Terms fetched successfully",
//                        value
//                )
//        );
//    }
//
//    @GetMapping("/policy")
//    public ResponseEntity<ApiResponse<String>> getPolicy() {
//
//        String value = service.getPublicSettingByKey("policy");
//
//        return ResponseEntity.ok(
//                new ApiResponse<>(
//                        true,
//                        "Policy fetched successfully",
//                        value
//                )
//        );
//    }
@GetMapping("/{category}")
public ResponseEntity<ApiResponse<String>> getPublicSetting(
        @PathVariable String category,
        @RequestParam(defaultValue = "en") String lang
) {
    String value = service.getPublicSettingByCategory(category, lang);

    return ResponseEntity.ok(
            new ApiResponse<>(
                    true,
                    category + " fetched successfully",
                    value
            )
    );
}
    @GetMapping("/general")
    public ResponseEntity<ApiResponse<GeneralSettingResponse>> getGeneralSettings() {

        GeneralSettingResponse response = service.getGeneralSettings();

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "General settings fetched successfully",
                        response
                )
        );
    }

    @GetMapping("/packages")
    public ResponseEntity<ApiResponse<List<PackageResponseDTO>>> getPackages(
            @RequestParam(defaultValue = "en") String lang
    ) {
        List<PackageResponseDTO> packages = packageService.getAll().stream()
                .filter(pkg -> Boolean.TRUE.equals(pkg.getIsActive()))
                .map(pkg -> {
                    PackageTranslation translation = packageService.resolveTranslationByLocale(pkg.getTranslations(), lang);
                    return packageMapper.toResponse(pkg, translation);
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Packages fetched successfully",
                        packages
                )
        );
    }
}
