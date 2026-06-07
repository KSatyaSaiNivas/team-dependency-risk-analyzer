package com.riskanalyzer.controller;

import com.riskanalyzer.dto.response.ApiResponse;
import com.riskanalyzer.dto.response.DashboardSummaryResponse;
import com.riskanalyzer.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/summary")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<DashboardSummaryResponse>> getSummary() {

        DashboardSummaryResponse response =
                dashboardService.getDashboardSummary();
        return ResponseEntity.ok(
                ApiResponse.success("Dashboard summary fetched", response));
    }
}