package com.example.SaaS.superAdmin.setting;

import com.example.SaaS.common.entity.Setting;
import com.example.SaaS.common.enums.SettingCategory;
import com.example.SaaS.landPage.GeneralSettingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SettingService {

    private final SettingRepository repository;
    private final SettingMapper mapper;

//    public SettingResourceDTO createOrUpdate(SettingRequest dto) {
//
//        Setting setting = repository
//                .findByCategoryAndKeyAndAdminId(
//                        dto.getCategory(),
//                        dto.getKey(),
//                        dto.getAdminId(),
//                        dto.getCompanyId()
//                )
//                .orElseGet(() -> mapper.toEntity(dto));
//
//        mapper.updateEntity(setting, dto);
//
//        return mapper.toResponse(repository.save(setting));
//    }

    /**
     * UPSERT: if the setting exists, update it; otherwise, insert a new row.
     */
    public SettingResourceDTO createOrUpdate(SettingRequest dto) {

        // 1️⃣ Try to find existing setting by unique keys
        Setting setting = repository
                .findByCategoryAndKeyAndAdminId(
                        dto.getCategory(),
                        dto.getKey(),
                        dto.getAdminId(),
                        dto.getCompanyId()
                )
                .orElseGet(() -> mapper.toEntity(dto)); // create new if not found

        // 2️⃣ Update entity fields with the DTO
        mapper.updateEntity(setting, dto);

        // 3️⃣ Save to DB (insert if new, update if exists)
        Setting saved = repository.save(setting);

        // 4️⃣ Convert to response DTO
        return mapper.toResponse(saved);
    }
    public List<SettingResourceDTO> getAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }
//    public String getPublicSettingByKey(String key) {
//
//        return repository.findByKeyAndAdminIdIsNull(key)
//                .map(Setting::getValue)
//                .orElseThrow(() -> new RuntimeException("Setting not found"));
//    }
//    public SettingResourceDTO getById(Long id) {
//        return mapper.toResponse(
//                repository.findById(id)
//                        .orElseThrow(() -> new RuntimeException("Setting not found"))
//        );
//    }
//
//    public void delete(Long id) {
//        repository.deleteById(id);
//    }

//    public String getPublicSettingByKey(String key) {
//
//        return repository
//                .findByCategoryAndKey(SettingCategory.GENERAL, key)
//                .map(Setting::getValue)
//                .orElse(null);
//    }

    public String getPublicSettingByCategory(String categoryStr, String lang) {

        // Convert string to enum
        SettingCategory category;
        try {
            category = SettingCategory.valueOf(categoryStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid category: " + categoryStr);
        }

        // Construct key with language
        String key = categoryStr.toLowerCase() + "_" + lang.toLowerCase(); // about_en, about_ar

        // Query by enum category and key
        return repository.findByCategoryAndKey(category, key)
                .map(setting -> setting.getValue())
                .orElseThrow(() -> new RuntimeException("Setting not found: " + key));
    }
    /**
     * Get public setting value by category/key and language.
     */
//    public String getPublicSettingByKey(String key, String lang) {
//
//        // Map the language to the key suffix
//        String keyWithLang = key + "_" + lang.toLowerCase(); // e.g., ABOUT_EN, ABOUT_AR
//
//        // Try to find setting by category and key
//        Setting setting = repository.findByKey(keyWithLang)
//                .orElseThrow(() -> new RuntimeException("Setting not found: " + keyWithLang));
//
//        return setting.getValue();
//    }
    public GeneralSettingResponse getGeneralSettings() {

        List<String> keys = List.of("phone", "email", "lat", "lng");

        List<Setting> settings =
                repository.findByCategoryAndKeyIn(
                        SettingCategory.GENERAL,
                        keys
                );

        Map<String, String> map = settings.stream()
                .collect(Collectors.toMap(
                        Setting::getKey,
                        Setting::getValue
                ));

        return GeneralSettingResponse.builder()
                .phone(map.get("phone"))
                .email(map.get("email"))
                .lat(map.get("lat"))
                .lng(map.get("lng"))
                .build();
    }
}