package com.example.SaaS.superAdmin.packagee;

import com.example.SaaS.common.dto.ApiResponse;
import com.example.SaaS.common.entity.Package;
import com.example.SaaS.common.entity.PackageTranslation;
import jakarta.validation.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/admin/packages")
@RequiredArgsConstructor
public class PackageController {
    private final PackageService packageService;
    private final PackageMapper  packageMapper;

    @PostMapping
    public ResponseEntity<ApiResponse<PackageResponseDTO>> create(
            @Valid  @RequestBody PackageRequest request
    ){

        Package pkg = Package.builder()
                .price(request.getPrice())
                .duration(request.getDuration())
                .ordering(request.getOrdering())
                .maxOrder(request.getMaxOrder())
                .maxProducts(request.getMaxProducts())
                .isActive(true)
                .build();
        List<PackageTranslation> translations =List.of(
                PackageTranslation.builder()
                        .locale("en")
                        .name(request.getNameEn())
                        .description(request.getDescriptionEn())
                        .pkg(pkg)
                         .build(),
                PackageTranslation.builder()
                        .locale("ar")
                        .name(request.getNameAr())
                        .description(request.getDescriptionAr())
                        .pkg(pkg)
                .build()

        );
        pkg.setTranslations(translations);
        Package saved = packageService.create(pkg);

        PackageTranslation translation =
                packageService.resolveTranslationByLocale(
                        saved.getTranslations(), "en"
                );

        return ResponseEntity.ok(
                new ApiResponse<PackageResponseDTO>(
                        true,
                        "Package created successfully",
                        packageMapper.toResponse(saved, translation)
                )
        );
    }


    @GetMapping
    public ResponseEntity<ApiResponse<List<PackageResponseDTO>>> getAll(@RequestParam(defaultValue = "en" ) String lang){
        List<PackageResponseDTO> response =
                packageService.getAll().stream().
                        map(pkg->{
                            PackageTranslation translation =
                                    packageService.resolveTranslationByLocale(pkg.getTranslations() ,lang);
                            return packageMapper.toResponse(pkg , translation);
                        })
                        .toList();
        return ResponseEntity.ok(new ApiResponse<>(true , "package retrived successfully"  ,response));
    }

    /* ==========================================================
   GET PACKAGE BY ID
   ========================================================== */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PackageResponseDTO>> getById(
            @PathVariable Long id,
            @RequestParam(defaultValue = "en") String lang
    ) {

        Package pkg = packageService.getById(id);

        PackageTranslation translation =
                packageService.resolveTranslationByLocale(
                        pkg.getTranslations(), lang
                );

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Package retrieved successfully",
                        packageMapper.toResponse(pkg, translation)
                )
        );
    }


    /* ==========================================================
     UPDATE PACKAGE
     ========================================================== */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PackageResponseDTO>> update(
            @PathVariable Long id,
            @Valid @RequestBody PackageRequest request
    ) {

//        Long adminId = SecurityUtil.getCurrentAdminId();pu

        Package existing = packageService.getById(id);

        // 🔒 Optional ownership / role check
//        if (!existing.getAddedBy().equals(adminId)) {
//            throw new BusinessException("You are not allowed to update this package");
//        }

        /* ================= UPDATE BASIC FIELDS ================= */
        existing.setPrice(request.getPrice());
        existing.setDuration(request.getDuration());
        existing.setOrdering(request.getOrdering());
//        existing.setFeatures(request.getFeatures());
        existing.setMaxOrder(request.getMaxOrder());
        existing.setMaxProducts(request.getMaxProducts());

        /* ================= UPDATE TRANSLATIONS (NO DELETE) ================= */
        packageService.upsertTranslation(
                existing,
                "en",
                request.getNameEn(),
                request.getDescriptionEn()
        );

        packageService.upsertTranslation(
                existing,
                "ar",
                request.getNameAr(),
                request.getDescriptionAr()
        );

        Package updated = packageService.update(id ,existing);

        PackageTranslation translation =
                packageService.resolveTranslationByLocale(
                        updated.getTranslations(), "en"
                );

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Package updated successfully",
                        packageMapper.toResponse(updated, translation)
                )
        );
    }


    /* ==========================================================
       DELETE PACKAGE
       ========================================================== */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {

//        Long adminId = SecurityUtil.getCurrentAdminId();

        Package pkg = packageService.getById(id);

//        // 🔒 Optional ownership check
//        if (!pkg.getAddedBy().equals(adminId)) {
//            throw new BusinessException("You are not allowed to delete this package");
//        }

        packageService.delete(id);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Package deleted successfully", null)
        );
    }

    /* ==========================================================
       ACTIVATE / DEACTIVATE PACKAGE
       ========================================================== */
    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<Void>> changeStatus(
            @PathVariable Long id,
            @RequestParam boolean active
    ) {

        Package pkg = packageService.getById(id);
        pkg.setIsActive(active);

        packageService.update(id,pkg);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Package status updated", null)
        );
    }

}
