package com.example.SaaS.company.faq.DTOS;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FaqRequest {

    private Long companyId;

    @NotBlank(message = "Question (Arabic) is required")
    @Size(min = 10, max = 1000, message = "Question must be between 10 and 1000 characters")
    private String questionAr;

    @NotBlank(message = "Question (English) is required")
    @Size(min = 10, max = 1000, message = "Question must be between 10 and 1000 characters")
    private String questionEn;

    @NotBlank(message = "Answer (Arabic) is required")
    @Size(min = 10, max = 5000, message = "Answer must be between 10 and 5000 characters")
    private String answerAr;

    @NotBlank(message = "Answer (English) is required")
    @Size(min = 10, max = 5000, message = "Answer must be between 10 and 5000 characters")
    private String answerEn;

    @NotNull(message = "Active flag is required")
    private Boolean isActive;

    private Integer ordering;

}
