package com.example.SaaS.superAdmin.dashboard.controller;

import com.example.SaaS.common.dto.ApiResponse;
import com.example.SaaS.superAdmin.dashboard.dto.DashboardStatisticsResponse;
import com.example.SaaS.superAdmin.dashboard.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/admin/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/statistics")
    public ResponseEntity<ApiResponse<DashboardStatisticsResponse>> statistics(){

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Statistics retrieved successfully", dashboardService.statistics())
        );
    }
}
