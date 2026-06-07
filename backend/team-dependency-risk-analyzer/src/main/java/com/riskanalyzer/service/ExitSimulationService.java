package com.riskanalyzer.service;

import com.riskanalyzer.dto.response.ExitSimulationResponse;

public interface ExitSimulationService {
    ExitSimulationResponse simulateExit(Long employeeId, Long projectId);
}