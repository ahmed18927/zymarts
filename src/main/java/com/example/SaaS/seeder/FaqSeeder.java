package com.example.SaaS.seeder;

import com.example.SaaS.common.entity.Company;
import com.example.SaaS.common.entity.Faq;
import com.example.SaaS.common.entity.FaqTranslation;
import com.example.SaaS.company.CompanyRepository;
import com.example.SaaS.company.faq.FaqRepository;
import com.example.SaaS.company.faq.FaqTranslationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Order(9)
@Component
@RequiredArgsConstructor
public class FaqSeeder implements CommandLineRunner {

    private final FaqRepository faqRepository;
    private final FaqTranslationRepository translationRepository;
    private final CompanyRepository companyRepository;

    @Override
    public void run(String... args) {
        System.out.println("Running FaqSeeder...");

        if (faqRepository.count() > 0) {
            System.out.println("FAQs already exist. Skipping seeding.");
            return;
        }

        List<Company> companies = companyRepository.findAll();
        List<Faq> faqs = new ArrayList<>();
        List<FaqTranslation> translations = new ArrayList<>();

        // Global FAQs (no company)
        Faq faq1 = Faq.builder()
                .company(null)
                .question("How do I create an account?")
                .answer("Click on the Sign Up button and fill in your details.")
                .isActive(true)
                .ordering(1)
                .build();

        faqs.add(faq1);

        translations.add(FaqTranslation.builder()
                .faq(faq1)
                .langCode("ar")
                .question("كيف أقوم بإنشاء حساب؟")
                .answer("اضغط على زر التسجيل واملأ بياناتك.")
                .build());

        translations.add(FaqTranslation.builder()
                .faq(faq1)
                .langCode("en")
                .question("How do I create an account?")
                .answer("Click on the Sign Up button and fill in your details.")
                .build());

        Faq faq2 = Faq.builder()
                .company(null)
                .question("What payment methods do you accept?")
                .answer("We accept credit cards, PayPal, and bank transfers.")
                .isActive(true)
                .ordering(2)
                .build();

        faqs.add(faq2);

        translations.add(FaqTranslation.builder()
                .faq(faq2)
                .langCode("ar")
                .question("ما هي طرق الدفع المقبولة؟")
                .answer("نقبل بطاقات الائتمان، PayPal، والتحويلات البنكية.")
                .build());

        translations.add(FaqTranslation.builder()
                .faq(faq2)
                .langCode("en")
                .question("What payment methods do you accept?")
                .answer("We accept credit cards, PayPal, and bank transfers.")
                .build());

        // Company-specific FAQs
        if (!companies.isEmpty()) {
            Company company1 = companies.get(0);

            Faq faq3 = Faq.builder()
                    .company(company1)
                    .question("What are your business hours?")
                    .answer("We are available from 9 AM to 6 PM, Monday to Friday.")
                    .isActive(true)
                    .ordering(1)
                    .build();

            faqs.add(faq3);

            translations.add(FaqTranslation.builder()
                    .faq(faq3)
                    .langCode("ar")
                    .question("ما هي ساعات العمل لديكم؟")
                    .answer("نحن متاحون من الساعة 9 صباحاً حتى 6 مساءً، من الاثنين إلى الجمعة.")
                    .build());

            translations.add(FaqTranslation.builder()
                    .faq(faq3)
                    .langCode("en")
                    .question("What are your business hours?")
                    .answer("We are available from 9 AM to 6 PM, Monday to Friday.")
                    .build());

            Faq faq4 = Faq.builder()
                    .company(company1)
                    .question("Do you offer refunds?")
                    .answer("Yes, we offer full refunds within 30 days of purchase.")
                    .isActive(true)
                    .ordering(2)
                    .build();

            faqs.add(faq4);

            translations.add(FaqTranslation.builder()
                    .faq(faq4)
                    .langCode("ar")
                    .question("هل تقدمون استرداد الأموال؟")
                    .answer("نعم، نقدم استرداد كامل للأموال خلال 30 يوماً من الشراء.")
                    .build());

            translations.add(FaqTranslation.builder()
                    .faq(faq4)
                    .langCode("en")
                    .question("Do you offer refunds?")
                    .answer("Yes, we offer full refunds within 30 days of purchase.")
                    .build());
        }

        if (companies.size() > 1) {
            Company company2 = companies.get(1);

            Faq faq5 = Faq.builder()
                    .company(company2)
                    .question("How can I track my order?")
                    .answer("You will receive a tracking link via email once your order ships.")
                    .isActive(true)
                    .ordering(1)
                    .build();

            faqs.add(faq5);

            translations.add(FaqTranslation.builder()
                    .faq(faq5)
                    .langCode("ar")
                    .question("كيف يمكنني تتبع طلبي؟")
                    .answer("ستتلقى رابط التتبع عبر البريد الإلكتروني بمجرد شحن طلبك.")
                    .build());

            translations.add(FaqTranslation.builder()
                    .faq(faq5)
                    .langCode("en")
                    .question("How can I track my order?")
                    .answer("You will receive a tracking link via email once your order ships.")
                    .build());
        }

        // Save FAQs first
        faqRepository.saveAll(faqs);

        // Then save translations
        translationRepository.saveAll(translations);

        System.out.println("FAQs and translations seeded successfully. Total FAQs: " + faqs.size());
    }
}
