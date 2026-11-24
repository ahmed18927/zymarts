package com.example.SaaS.superAdmin.setting;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/admin/setting")
public class SettingController {

    private final SettingService service;

    // ✅ UPSERT
    @PostMapping("/upsert")
    public SettingResourceDTO upsert(@ModelAttribute SettingRequest dto) {
        return service.createOrUpdate(dto);
    }

    // ✅ LIST
    @GetMapping
    public List<SettingResourceDTO> list() {
        return service.getAll();
    }

//    // ✅ SHOW
//    @GetMapping("/{id}")
//    public SettingResourceDTO show(@PathVariable Long id) {
//        return service.getById(id);
//    }
//
//    // ✅ DELETE
//    @DeleteMapping("/{id}")
//    public void delete(@PathVariable Long id) {
//        service.delete(id);
//    }
}
