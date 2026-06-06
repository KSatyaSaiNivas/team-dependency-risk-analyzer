package com.riskanalyzer.dto.response;

import com.riskanalyzer.enums.Role;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UserResponse {
    private Long id;
    private String email;
    private Role role;
    private Boolean isActive;
    private LocalDateTime createdAt;
}