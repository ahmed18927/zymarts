package com.example.SaaS.seeder;

import com.example.SaaS.common.entity.City;
import com.example.SaaS.common.entity.Country;
import com.example.SaaS.superAdmin.country.CountryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Map;
@Order(2)
@Component
@RequiredArgsConstructor
public class CountrySeeder implements CommandLineRunner {

    private final CountryRepository countryRepository;

    @Override
    public void run(String... args) {
        System.out.println("Running CountryCitySeeder...");

        if (countryRepository.count() > 0) {
            System.out.println("Countries already exist. Skipping seeding.");
            return;
        }

        Instant now = Instant.now();

        /* ================= EGYPT ================= */
        Country egypt = Country.builder()
                .name(Map.of("ar", "مصر", "en", "Egypt"))
                .code("EG")
                .flag("🇪🇬")
                .createdAt(now)
                .updatedAt(now)
                .build();

        egypt.getCities().addAll(List.of(
                City.builder()
                        .name(Map.of("ar", "القاهرة", "en", "Cairo"))
                        .country(egypt)
                        .createdAt(now)
                        .updatedAt(now)
                        .build(),
                City.builder()
                        .name(Map.of("ar", "الإسكندرية", "en", "Alexandria"))
                        .country(egypt)
                        .createdAt(now)
                        .updatedAt(now)
                        .build()
        ));

        /* ================= SAUDI ================= */
        Country saudi = Country.builder()
                .name(Map.of("ar", "السعودية", "en", "Saudi Arabia"))
                .code("SA")
                .flag("🇸🇦")
                .createdAt(now)
                .updatedAt(now)
                .build();

        saudi.getCities().addAll(List.of(
                City.builder()
                        .name(Map.of("ar", "الرياض", "en", "Riyadh"))
                        .country(saudi)
                        .createdAt(now)
                        .updatedAt(now)
                        .build(),
                City.builder()
                        .name(Map.of("ar", "جدة", "en", "Jeddah"))
                        .country(saudi)
                        .createdAt(now)
                        .updatedAt(now)
                        .build()
        ));

        /* ================= UAE ================= */
        Country uae = Country.builder()
                .name(Map.of("ar", "الإمارات", "en", "United Arab Emirates"))
                .code("AE")
                .flag("🇦🇪")
                .createdAt(now)
                .updatedAt(now)
                .build();

        uae.getCities().addAll(List.of(
                City.builder()
                        .name(Map.of("ar", "دبي", "en", "Dubai"))
                        .country(uae)
                        .createdAt(now)
                        .updatedAt(now)
                        .build(),
                City.builder()
                        .name(Map.of("ar", "أبو ظبي", "en", "Abu Dhabi"))
                        .country(uae)
                        .createdAt(now)
                        .updatedAt(now)
                        .build()
        ));

        countryRepository.saveAll(List.of(egypt, saudi, uae));

        System.out.println("Countries & Cities seeded successfully.");
    }
}