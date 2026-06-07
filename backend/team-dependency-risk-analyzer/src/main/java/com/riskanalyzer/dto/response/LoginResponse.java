package com.riskanalyzer.dto.response;

import com.riskanalyzer.enums.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {
    private String token;
    private String email;
    private Role role;
    private String message;
}