package com.example.SaaS.superAdmin.setting;

import com.example.SaaS.common.entity.Setting;
import lombok.Builder;
import org.springframework.stereotype.Component;

@Component
public class SettingMapper {

    public Setting toEntity(SettingRequest dto) {
        if (dto == null) return null;

        return Setting.builder()
                .category(dto.getCategory())
                .key(dto.getKey())
                .value(dto.getValue())
                .companyId(dto.getCompanyId())
                .adminId(dto.getAdminId())
                .build();
    }

    public SettingResourceDTO toResponse(Setting setting) {
        if (setting == null) return null;

        return SettingResourceDTO.builder()
                .id(setting.getId())
                .category(setting.getCategory())
                .key(setting.getKey())
                .value(setting.getValue())
                .companyId(setting.getCompanyId())
                .adminId(setting.getAdminId())
                .build();
    }

    public void updateEntity(Setting setting, SettingRequest dto) {
        if (dto.getCategory() != null)
            setting.setCategory(dto.getCategory());
        if (dto.getKey() != null)
            setting.setKey(dto.getKey());
        if (dto.getValue() != null)
            setting.setValue(dto.getValue());
        if (dto.getCompanyId() != null)
            setting.setCompanyId(dto.getCompanyId());
        if (dto.getAdminId() != null)
            setting.setAdminId(dto.getAdminId());
    }
}