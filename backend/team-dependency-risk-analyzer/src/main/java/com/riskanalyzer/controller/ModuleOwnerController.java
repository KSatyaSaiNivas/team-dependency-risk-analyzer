package com.riskanalyzer.controller;

import com.riskanalyzer.dto.request.AssignOwnerRequest;
import com.riskanalyzer.dto.response.ApiResponse;
import com.riskanalyzer.dto.response.EmployeeResponse;
import com.riskanalyzer.service.ModuleOwnerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/module-owners")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ModuleOwnerController {

    private final ModuleOwnerService moduleOwnerService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Void>> assignOwner(
            @Valid @RequestBody AssignOwnerRequest request) {

        moduleOwnerService.assignOwner(request);
        return ResponseEntity.ok(
                ApiResponse.success("Owner assigned successfully", null));
    }

    @GetMapping("/module/{moduleId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<EmployeeResponse>>> getOwnersByModule(
            @PathVariable Long moduleId) {

        List<EmployeeResponse> owners =
                moduleOwnerService.getOwnersByModule(moduleId);
        return ResponseEntity.ok(
                ApiResponse.success("Owners fetched successfully", owners));
    }

    @DeleteMapping("/{moduleOwnerId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Void>> removeOwner(
            @PathVariable Long moduleOwnerId) {

        moduleOwnerService.removeOwner(moduleOwnerId);
        return ResponseEntity.ok(
                ApiResponse.success("Owner removed successfully", null));
    }
}