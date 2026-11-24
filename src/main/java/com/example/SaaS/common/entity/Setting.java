package com.example.SaaS.common.entity;

import com.example.SaaS.common.enums.SettingCategory;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "settings",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {
                        "category","key_name","company_id","admin_id"
                })
        })
public class Setting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private SettingCategory category;

    @Column(name = "setting_key")
    private String key;

    @Column(columnDefinition = "TEXT")
    private String value;

    private Long companyId;

    private Long adminId;
}
