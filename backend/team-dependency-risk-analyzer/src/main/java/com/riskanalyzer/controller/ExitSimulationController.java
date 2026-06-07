package com.riskanalyzer.controller;

import com.riskanalyzer.dto.response.ApiResponse;
import com.riskanalyzer.dto.response.ExitSimulationResponse;
import com.riskanalyzer.service.ExitSimulationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/simulation")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ExitSimulationController {

    private final ExitSimulationService exitSimulationService;

    @GetMapping("/exit")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<ExitSimulationResponse>> simulateExit(
            @RequestParam Long employeeId,
            @RequestParam Long projectId) {

        ExitSimulationResponse response =
                exitSimulationService.simulateExit(employeeId, projectId);
        return ResponseEntity.ok(
                ApiResponse.success(
                        "Exit simulation completed for: "
                                + response.getEmployeeName(),
                        response));
    }
}