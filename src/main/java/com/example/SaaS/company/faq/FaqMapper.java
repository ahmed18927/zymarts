package com.example.SaaS.company.faq;

import com.example.SaaS.common.entity.Faq;
import com.example.SaaS.company.faq.DTOS.FaqResponse;
import org.springframework.stereotype.Component;

@Component
public class FaqMapper {

    public FaqResponse toResponse( Faq faq, String question, String answer){
        if(faq == null){
            return null;
        }
        return FaqResponse.builder()
                .id(faq.getId())
                .companyId(faq.getCompany() != null ? faq.getCompany().getId() : null)
                .companyName(faq.getCompany() != null ? faq.getCompany().getName() : null)
                .question(question)
                .answer(answer)
                .isActive(faq.getIsActive())
                .ordering(faq.getOrdering())
                .build();
    }
}
