package com.example.SaaS.seeder;

import com.example.SaaS.common.entity.Banner;
import com.example.SaaS.company.banner.BannerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BannerSeeder implements CommandLineRunner {

    private final BannerRepository bannerRepository;

    @Override
    public void run(String... args) throws Exception {

        if (bannerRepository.count() > 0) {
            return; // prevent duplicate seeding
        }

        Banner banner1 = new Banner();
        banner1.setOrdering(1);
        banner1.setIsActive(true);
        banner1.setLink("https://www.google.com/");
        banner1.setStartDate(LocalDate.now());
        banner1.setEndDate(LocalDate.now().plusMonths(1));
//        banner1.setCategoryId(1L);
        banner1.setImage("images/banner1.jpg");

        Banner banner2 = new Banner();
        banner2.setOrdering(2);
        banner2.setIsActive(true);
        banner2.setLink("https://www.google.com/");
        banner2.setStartDate(LocalDate.now());
        banner2.setEndDate(LocalDate.now().plusMonths(2));
//        banner2.setCategoryId(2L);
        banner2.setImage("images/banner2.jpg");

        bannerRepository.saveAll(List.of(banner1, banner2));

        System.out.println("✅ Banner seed data inserted.");
    }

}
