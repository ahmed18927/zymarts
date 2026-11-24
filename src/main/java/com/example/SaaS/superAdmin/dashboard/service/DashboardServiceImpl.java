package com.example.SaaS.superAdmin.dashboard.service;

import com.example.SaaS.superAdmin.dashboard.dto.DashboardStatisticsResponse;
import com.example.SaaS.superAdmin.dashboard.repository.DashboardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService{
    private final DashboardRepository repository;

    @Override
    public DashboardStatisticsResponse statistics() {

        Instant today =
                LocalDate.now()
                        .atStartOfDay()
                        .toInstant(ZoneOffset.UTC);

        return DashboardStatisticsResponse.builder()

                .totalCompanies(
                        repository.totalCompanies()
                )

                .newCompaniesToday(
                        repository.newCompaniesToday(today)
                )

                .activeSubscriptions(
                        repository.activeSubscriptions()
                )

                .expiredSubscriptions(
                        repository.expiredSubscriptions(LocalDateTime.now())
                )

                .totalOrders(
                        repository.totalOrders()
                )

                .pendingOrders(
                        repository.pendingOrders()
                )

                .completedOrders(
                        repository.completedOrders()
                )

                .build();

    }

}
