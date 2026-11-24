package com.example.SaaS.superAdmin.setting;

import com.example.SaaS.common.enums.SettingCategory;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class SettingResourceDTO {
    private Long id;
    private SettingCategory category;
    private String key;
    private String value;
    private Long companyId;
    private Long adminId;
}
