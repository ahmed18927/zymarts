package com.example.SaaS.seeder;

import com.example.SaaS.common.entity.Setting;
import com.example.SaaS.common.enums.SettingCategory;
import com.example.SaaS.superAdmin.setting.SettingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SettingSeeder implements CommandLineRunner {
    private final SettingRepository repository;

    @Override
    public void run(String... args) {

        seed(SettingCategory.ABOUT, "about_en", "About EN");
        seed(SettingCategory.ABOUT, "about_ar", "عن الشركة");

        seed(SettingCategory.TERMS, "terms_en", "Terms EN");
        seed(SettingCategory.TERMS, "terms_ar", "الشروط");

        seed(SettingCategory.PRIVACY, "privacy_en", "Privacy EN");
        seed(SettingCategory.PRIVACY, "privacy_ar", "سياسة الخصوصية");

        seed(SettingCategory.GENERAL, "email", "admin@company.com");
        seed(SettingCategory.GENERAL, "phone", "0100000000");
    }

    private void seed(SettingCategory category, String key, String value) {

        repository
                .findByCategoryAndKeyAndAdminId(
                        category, key, null, 1L
                )
                .orElseGet(() ->
                        repository.save(
                                Setting.builder()
                                        .category(category)
                                        .key(key)
                                        .value(value)
                                        .adminId(1L)
                                        .build()
                        )
                );
    }
}
