package com.example.SaaS.company.faq;

import com.example.SaaS.common.entity.Company;
import com.example.SaaS.common.entity.Faq;
import com.example.SaaS.common.entity.FaqTranslation;
import com.example.SaaS.common.exception.BusinessException;
import com.example.SaaS.company.CompanyRepository;
import com.example.SaaS.company.faq.DTOS.FaqRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FaqService {

    private final FaqRepository faqRepository;
    private final FaqTranslationRepository translationRepository;
    private final CompanyRepository companyRepository;

    @Transactional
    public Faq create (FaqRequest request){
        Company company = null;
        if (request.getCompanyId() != null) {
            company = companyRepository.findById(request.getCompanyId())
                    .orElseThrow(() -> new BusinessException("Company not found"));
        }
        Faq faq = Faq.builder()
                .company(company)
                .question(request.getQuestionEn())
                .answer(request.getAnswerEn())
                .isActive(true)
                .ordering(request.getOrdering() != null ? request.getOrdering() : 0)
                .build();

        faq = faqRepository.save(faq);

        FaqTranslation translationAr = FaqTranslation.builder()
                .faq(faq)
                .langCode("ar")
                .question(request.getQuestionAr())
                .answer(request.getAnswerAr())
                .build();
        translationRepository.save(translationAr);

        FaqTranslation translationEn = FaqTranslation.builder()
                .faq(faq)
                .langCode("en")
                .question(request.getQuestionEn())
                .answer(request.getAnswerEn())
                .build();
        translationRepository.save(translationEn);

        return faq;
    }

    @Transactional
    public Faq update (Long id, FaqRequest request){
        Faq faq = faqRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Faq not found"));

        if (request.getCompanyId() != null){
            Company company = companyRepository.findById(request.getCompanyId())
                    .orElseThrow(() -> new BusinessException("Company not found"));
            faq.setCompany(company);
        }

        faq.setQuestion(request.getQuestionEn());
        faq.setAnswer(request.getAnswerEn());
        faq.setIsActive(request.getIsActive());
        faq.setOrdering(request.getOrdering() != null ? request.getOrdering() : 0);

        // Update Arabic translation
        FaqTranslation translationAr = translationRepository
                .findByFaqAndLangCode(faq, "ar")
                .orElse(FaqTranslation.builder()
                        .faq(faq)
                        .langCode("ar")
                        .build());
        translationAr.setQuestion(request.getQuestionAr());
        translationAr.setAnswer(request.getAnswerAr());
        translationRepository.save(translationAr);

        // Update English translation
        FaqTranslation translationEn = translationRepository
                .findByFaqAndLangCode(faq, "en")
                .orElse(FaqTranslation.builder()
                        .faq(faq)
                        .langCode("en")
                        .build());
        translationEn.setQuestion(request.getQuestionEn());
        translationEn.setAnswer(request.getAnswerEn());
        translationRepository.save(translationEn);

        return faqRepository.save(faq);
    }

    public Faq getById(Long id) {
        return faqRepository.findById(id)
                .orElseThrow(() -> new BusinessException("FAQ not found"));
    }

    public Page<Faq> getAll(Pageable pageable) {
        return faqRepository.findAll(pageable);
    }

    public List<Faq> getActiveOnly() {
        return faqRepository.findByIsActiveTrueOrderByOrderingAsc();
    }

    public List<Faq> getByCompanyId(Long companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new BusinessException("Company not found"));
        return faqRepository.findByCompanyOrderByOrderingAsc(company);
    }

    public List<Faq> getActiveByCompanyId(Long companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new BusinessException("Company not found"));
        return faqRepository.findByCompanyAndIsActiveTrueOrderByOrderingAsc(company);
    }

    @Transactional
    public void delete(Long id) {
        Faq faq = getById(id);
        faqRepository.delete(faq);
    }

    public String getTranslatedQuestion(Faq faq, String lang) {
        return translationRepository.findByFaqAndLangCode(faq, lang)
                .map(FaqTranslation::getQuestion)
                .orElse(translationRepository.findByFaqAndLangCode(faq, "en")
                        .map(FaqTranslation::getQuestion)
                        .orElse(faq.getQuestion()));
    }

    public String getTranslatedAnswer(Faq faq, String lang) {
        return translationRepository.findByFaqAndLangCode(faq, lang)
                .map(FaqTranslation::getAnswer)
                .orElse(translationRepository.findByFaqAndLangCode(faq, "en")
                        .map(FaqTranslation::getAnswer)
                        .orElse(faq.getAnswer()));
    }
}
