package com.riskanalyzer.dto.request;

import com.riskanalyzer.enums.OwnershipType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AssignOwnerRequest {

    @NotNull(message = "Module ID is required")
    private Long moduleId;

    @NotNull(message = "Employee ID is required")
    private Long employeeId;

    @NotNull(message = "Ownership type is required")
    private OwnershipType ownershipType;
}