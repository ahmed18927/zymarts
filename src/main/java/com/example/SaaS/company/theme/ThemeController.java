package com.example.SaaS.company.theme;

import com.example.SaaS.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/company/themes")
public class ThemeController {
    private final ThemeService themeService;
    private final ThemeMapper themeMapper;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ThemeResourceDTO>>> getAllThemes() {
        List<ThemeResourceDTO> themes = themeService.getAvailableThemes()
                .stream()
                .map(themeMapper::toResponse)
                .toList();
        return ResponseEntity.ok(new ApiResponse<>(true, "retrived successfully", themes));
    }

    @PostMapping("/apply/{themeId}")
    public ResponseEntity<ApiResponse<Void>> applyTheme(@PathVariable Long themeId) {
        themeService.applyTheme(themeId);
        return ResponseEntity.ok(new ApiResponse<>(true, "theme applied successfully", null));
    }

    @PutMapping("/customize")
    public ResponseEntity<ApiResponse<Void>> customizeTheme(@Valid @RequestBody ThemeCustomizeRequest request) {
        themeService.customizeTheme(request);
        return ResponseEntity.ok(new ApiResponse<>(true, "theme customized successfully", null));
    }

}
