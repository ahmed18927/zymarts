package com.example.SaaS.company.faq;

import com.example.SaaS.common.entity.Faq;
import com.example.SaaS.common.response.ApiResponse;
import com.example.SaaS.company.faq.DTOS.FaqRequest;
import com.example.SaaS.company.faq.DTOS.FaqResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/faqs")
@RequiredArgsConstructor
public class FaqController {
    private final FaqService faqService;
    private final FaqMapper faqMapper;

    /* ============ CREATE ============ */
    @PostMapping
    public ResponseEntity<ApiResponse<FaqResponse>> create(
            @Valid @RequestBody FaqRequest request) {

        Faq created = faqService.create(request);

        FaqResponse response = faqMapper.toResponse(
                created,
                faqService.getTranslatedQuestion(created, "en"),
                faqService.getTranslatedAnswer(created, "en")
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "FAQ created successfully", response));
    }

    /* ============ GET ALL ============ */
    @GetMapping
    public ResponseEntity<ApiResponse<Page<FaqResponse>>> getAll(
            @RequestParam(defaultValue = "en") String lang,
            @PageableDefault(size = 10) Pageable pageable) {

        Page<FaqResponse> response = faqService.getAll(pageable)
                .map(faq -> faqMapper.toResponse(
                        faq,
                        faqService.getTranslatedQuestion(faq, lang),
                        faqService.getTranslatedAnswer(faq, lang)
                ));

        return ResponseEntity.ok(
                new ApiResponse<>(true, "FAQs retrieved successfully", response)
        );
    }

    /* ============ GET ACTIVE ONLY ============ */
    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<FaqResponse>>> getActiveOnly(
            @RequestParam(defaultValue = "en") String lang) {

        List<FaqResponse> response = faqService.getActiveOnly()
                .stream()
                .map(faq -> faqMapper.toResponse(
                        faq,
                        faqService.getTranslatedQuestion(faq, lang),
                        faqService.getTranslatedAnswer(faq, lang)
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Active FAQs retrieved successfully", response)
        );
    }

    /* ============ GET BY COMPANY ID ============ */
    @GetMapping("/company/{companyId}")
    public ResponseEntity<ApiResponse<List<FaqResponse>>> getByCompanyId(
            @PathVariable Long companyId,
            @RequestParam(defaultValue = "en") String lang) {

        List<FaqResponse> response = faqService.getByCompanyId(companyId)
                .stream()
                .map(faq -> faqMapper.toResponse(
                        faq,
                        faqService.getTranslatedQuestion(faq, lang),
                        faqService.getTranslatedAnswer(faq, lang)
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Company FAQs retrieved successfully", response)
        );
    }

    /* ============ GET ACTIVE BY COMPANY ID ============ */
    @GetMapping("/company/{companyId}/active")
    public ResponseEntity<ApiResponse<List<FaqResponse>>> getActiveByCompanyId(
            @PathVariable Long companyId,
            @RequestParam(defaultValue = "en") String lang) {

        List<FaqResponse> response = faqService.getActiveByCompanyId(companyId)
                .stream()
                .map(faq -> faqMapper.toResponse(
                        faq,
                        faqService.getTranslatedQuestion(faq, lang),
                        faqService.getTranslatedAnswer(faq, lang)
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Active company FAQs retrieved successfully", response)
        );
    }

    /* ============ GET BY ID ============ */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<FaqResponse>> getById(
            @PathVariable Long id,
            @RequestParam(defaultValue = "en") String lang) {

        Faq faq = faqService.getById(id);

        FaqResponse response = faqMapper.toResponse(
                faq,
                faqService.getTranslatedQuestion(faq, lang),
                faqService.getTranslatedAnswer(faq, lang)
        );

        return ResponseEntity.ok(
                new ApiResponse<>(true, "FAQ retrieved successfully", response)
        );
    }

    /* ============ UPDATE ============ */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<FaqResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody FaqRequest request) {

        Faq updated = faqService.update(id, request);

        FaqResponse response = faqMapper.toResponse(
                updated,
                faqService.getTranslatedQuestion(updated, "en"),
                faqService.getTranslatedAnswer(updated, "en")
        );

        return ResponseEntity.ok(
                new ApiResponse<>(true, "FAQ updated successfully", response)
        );
    }

    /* ============ DELETE ============ */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {

        faqService.delete(id);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "FAQ deleted successfully", null)
        );
    }
}
