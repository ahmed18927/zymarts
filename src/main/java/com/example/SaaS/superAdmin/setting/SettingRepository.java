package com.example.SaaS.superAdmin.setting;

import com.example.SaaS.common.entity.Setting;
import com.example.SaaS.common.enums.SettingCategory;
import org.springframework.data.jpa.repository.JpaRepository;

//import java.awt.*;
import java.util.Optional;
import java.util.List;

public interface SettingRepository extends JpaRepository<Setting,String> {
    Optional<Setting> findByCategoryAndKeyAndAdminId(
            SettingCategory category,
            String key,
            Long companyId,
            Long adminId
    );

    Optional<Setting> findByKeyAndAdminIdIsNull(String key);
    java.util.List<Setting> findByCategoryAndKeyIn(
            SettingCategory category,
            List<String> keys
    );
//    Optional<Setting> findByCategoryAndKey(
//            SettingCategory category,
//            String keyName
//    );
Optional<Setting> findByCategoryAndKey(SettingCategory category, String key);


    Optional<Setting> findByKey(
            String keyName
    );
}

