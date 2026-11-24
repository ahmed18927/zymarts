package com.example.SaaS.superAdmin.packagee;

import com.example.SaaS.common.entity.Package;
import com.example.SaaS.common.entity.PackageTranslation;
import lombok.Builder;
import org.springframework.stereotype.Component;

@Component
@Builder
public class PackageMapper {
    public PackageResponseDTO toResponse(
            Package pkg ,
            PackageTranslation translation
    ){
        return PackageResponseDTO.builder()
                .id(pkg.getId())
                .name(translation != null ? translation.getName() : null)
                .description(translation != null ?translation.getDescription():null)
                .price(pkg.getPrice())
                .duration(pkg.getDuration())
                .ordering(pkg.getOrdering())
                .isActive(pkg.getIsActive())
                .build();
    }
}
