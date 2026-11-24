package com.example.SaaS.company.faq.DTOS;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FaqResponse {
    private Long id;
    private Long companyId;
    private String companyName;
    private String question;
    private String answer;
    private Boolean isActive;
    private Integer ordering;
}