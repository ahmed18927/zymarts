package com.example.SaaS.superAdmin.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatisticsResponse {

    private Long totalCompanies;

    private Long newCompaniesToday;

    private Long activeSubscriptions;

    private Long expiredSubscriptions;

    private Long totalOrders;

    private Long pendingOrders;

    private Long completedOrders;
}
