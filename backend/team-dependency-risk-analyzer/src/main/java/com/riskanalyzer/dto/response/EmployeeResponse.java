package com.riskanalyzer.dto.response;

import com.riskanalyzer.enums.EmployeeStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class EmployeeResponse {
    private Long id;
    private String fullName;
    private String email;
    private String department;
    private String designation;
    private LocalDate joiningDate;
    private EmployeeStatus status;
    private LocalDateTime createdAt;
}