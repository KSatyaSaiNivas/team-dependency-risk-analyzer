package com.riskanalyzer.service;

import com.riskanalyzer.dto.request.LoginRequest;
import com.riskanalyzer.dto.response.LoginResponse;

public interface AuthService {
    LoginResponse login(LoginRequest request);
}