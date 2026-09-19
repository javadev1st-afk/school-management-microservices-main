package com.school.communicationservice.controller;

import com.school.communicationservice.dto.LeaveApplicationDTO;
import com.school.communicationservice.service.LeaveApplicationService;
import com.school.common.enums.LeaveStatus;
import com.school.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/leave-applications")
@RequiredArgsConstructor
@Tag(name = "Leave Applications", description = "Leave application management endpoints")
@SecurityRequirement(name = "bearerAuth")
public class LeaveApplicationController {
	private final LeaveApplicationService service;

	@PostMapping
	public ResponseEntity<ApiResponse<LeaveApplicationDTO>> apply(@Valid @RequestBody LeaveApplicationDTO dto) {
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(ApiResponse.success(service.apply(dto), "Leave application submitted successfully"));
	}

	@GetMapping("/{id}")
	public ApiResponse<LeaveApplicationDTO> get(@PathVariable Long id) {
		return ApiResponse.success(service.get(id));
	}

	@GetMapping("/student/{admissionNumber}")
	public ApiResponse<List<LeaveApplicationDTO>> byStudent(@PathVariable Long admissionNumber) {
		return ApiResponse.success(service.byStudentAdmissionNumber(admissionNumber));
	}
	@GetMapping("/student/{admissionNumber}/date/{date}")
	public ApiResponse<List<LeaveApplicationDTO>> byStudentAndDate(@PathVariable Long admissionNumber, @PathVariable LocalDate date) {
		return ApiResponse.success(service.byAdmissionNumberAndDate(admissionNumber, date));
	}
	
	@GetMapping("/year/all")
	@PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
	public ApiResponse<List<LeaveApplicationDTO>> allCurrentYear() {
		return ApiResponse.success(service.getCurrentYearLeave());
	}

	@GetMapping("/date/{date}")
	@PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
	public ApiResponse<List<LeaveApplicationDTO>> byDate(@PathVariable LocalDate date) {
		return ApiResponse.success(service.byDate(date));
	}
	
	
	@GetMapping
	@PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
	public ApiResponse<List<LeaveApplicationDTO>> allRecent(@RequestParam(defaultValue = "PENDING") LeaveStatus status) {
		return ApiResponse.success(service.byStatus(status));
	}
	

	@PostMapping("/{id}/decision/{status}")
	@PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
	public ApiResponse<LeaveApplicationDTO> decide(@PathVariable Long id, @PathVariable LeaveStatus status) {
		return ApiResponse.success(service.decide(id, status, ""),
				"Leave application updated successfully");
	}
}
