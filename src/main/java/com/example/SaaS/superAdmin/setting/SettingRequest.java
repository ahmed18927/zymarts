package com.example.SaaS.superAdmin.setting;

import com.example.SaaS.common.enums.SettingCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SettingRequest {
    private SettingCategory category;
    private String key;
    private String value;
    private Long companyId;
    private Long adminId;
}
