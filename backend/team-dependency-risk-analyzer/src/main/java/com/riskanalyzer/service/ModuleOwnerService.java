package com.riskanalyzer.service;

import com.riskanalyzer.dto.request.AssignOwnerRequest;
import com.riskanalyzer.dto.response.EmployeeResponse;

import java.util.List;

public interface ModuleOwnerService {
    void assignOwner(AssignOwnerRequest request);
    List<EmployeeResponse> getOwnersByModule(Long moduleId);
    void removeOwner(Long moduleOwnerId);
}