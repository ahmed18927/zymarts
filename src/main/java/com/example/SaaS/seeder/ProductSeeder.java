package com.example.SaaS.seeder;

import com.example.SaaS.common.entity.Product;
import com.example.SaaS.common.entity.ProductDetails;
import com.example.SaaS.common.entity.ProductTranslation;
import com.example.SaaS.company.product.ProductRepository;
import com.example.SaaS.company.product.ProductTranslationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Order(7)
@Component
@RequiredArgsConstructor
public class ProductSeeder implements CommandLineRunner {

    private final ProductRepository productRepository;
    private final ProductTranslationRepository translationRepository;

    @Override
    public void run(String... args) {
        System.out.println("Running ProductSeeder...");

        if (productRepository.count() > 0) {
            System.out.println("Products already exist. Skipping seeding.");
            return;
        }

        Instant now = Instant.now();
        List<Product> products = new ArrayList<>();

        // Product 1: Laptop
        Product product1 = Product.builder()
                .sellPrice(new BigDecimal("1200.00"))
                .stock(50)
                .isActive(true)
                .ordering(1)
                .buyingPrice(new BigDecimal("900.00"))
                .createdAt(now)
                .updatedAt(now)
                .build();

        ProductDetails details1 = ProductDetails.builder()
                .productId(product1)
                .sku("LAP-001")
                .mpn("MPN-LAP-001")
                .discountType("percentage")
                .discountValue(new BigDecimal("10.00"))
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(30))
                .views(0L)
                .soldQuentity(0L)
                .createdAt(now)
                .updatedAt(now)
                .build();

        product1.setProductDetails(details1);
        products.add(product1);

        // Product 2: Smartphone
        Product product2 = Product.builder()
                .sellPrice(new BigDecimal("800.00"))
                .stock(100)
                .isActive(true)
                .ordering(2)
                .buyingPrice(new BigDecimal("600.00"))
                .createdAt(now)
                .updatedAt(now)
                .build();

        ProductDetails details2 = ProductDetails.builder()
                .productId(product2)
                .sku("PHN-001")
                .mpn("MPN-PHN-001")
                .discountType("fixed")
                .discountValue(new BigDecimal("50.00"))
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(15))
                .views(0L)
                .soldQuentity(0L)
                .createdAt(now)
                .updatedAt(now)
                .build();

        product2.setProductDetails(details2);
        products.add(product2);

        // Product 3: Headphones
        Product product3 = Product.builder()
                .sellPrice(new BigDecimal("150.00"))
                .stock(200)
                .isActive(true)
                .ordering(3)
                .buyingPrice(new BigDecimal("100.00"))
                .createdAt(now)
                .updatedAt(now)
                .build();

        ProductDetails details3 = ProductDetails.builder()
                .productId(product3)
                .sku("HDP-001")
                .mpn("MPN-HDP-001")
                .discountType(null)
                .discountValue(null)
                .startDate(null)
                .endDate(null)
                .views(0L)
                .soldQuentity(0L)
                .createdAt(now)
                .updatedAt(now)
                .build();

        product3.setProductDetails(details3);
        products.add(product3);

        // Save products
        productRepository.saveAll(products);

        // Create translations
        List<ProductTranslation> translations = new ArrayList<>();

        // Translations for Product 1
        translations.add(ProductTranslation.builder()
                .product(product1)
                .langCode("ar")
                .name("لابتوب احترافي")
                .description("لابتوب عالي الأداء مثالي للعمل والألعاب")
                .specifications("معالج Intel Core i7، رام 16GB، SSD 512GB")
                .build());

        translations.add(ProductTranslation.builder()
                .product(product1)
                .langCode("en")
                .name("Professional Laptop")
                .description("High-performance laptop perfect for work and gaming")
                .specifications("Intel Core i7 Processor, 16GB RAM, 512GB SSD")
                .build());

        // Translations for Product 2
        translations.add(ProductTranslation.builder()
                .product(product2)
                .langCode("ar")
                .name("هاتف ذكي متطور")
                .description("هاتف ذكي بأحدث التقنيات وكاميرا عالية الجودة")
                .specifications("شاشة 6.5 بوصة، كاميرا 48 ميجابكسل، بطارية 5000 mAh")
                .build());

        translations.add(ProductTranslation.builder()
                .product(product2)
                .langCode("en")
                .name("Advanced Smartphone")
                .description("Smartphone with latest technology and high-quality camera")
                .specifications("6.5-inch display, 48MP camera, 5000 mAh battery")
                .build());

        // Translations for Product 3
        translations.add(ProductTranslation.builder()
                .product(product3)
                .langCode("ar")
                .name("سماعات لاسلكية")
                .description("سماعات بلوتوث مع إلغاء الضوضاء")
                .specifications("بلوتوث 5.0، عمر بطارية 30 ساعة، مقاومة للماء")
                .build());

        translations.add(ProductTranslation.builder()
                .product(product3)
                .langCode("en")
                .name("Wireless Headphones")
                .description("Bluetooth headphones with noise cancellation")
                .specifications("Bluetooth 5.0, 30-hour battery life, water-resistant")
                .build());

        translationRepository.saveAll(translations);

        System.out.println("Products and translations seeded successfully. Total products: " + products.size());
    }
}
