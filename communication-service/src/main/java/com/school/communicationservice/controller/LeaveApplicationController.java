package com.school.communicationservice.controller;

import com.school.communicationservice.dto.LeaveApplicationDTO;
import com.school.communicationservice.service.LeaveApplicationService;
import com.school.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
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
		return ApiResponse.success(service.byStudent(admissionNumber));
	}

	@GetMapping
	public ApiResponse<List<LeaveApplicationDTO>> byStatus(@RequestParam(defaultValue = "PENDING") String status) {
		return ApiResponse.success(service.byStatus(status));
	}

	@PatchMapping("/{id}/decision")
	public ApiResponse<LeaveApplicationDTO> decide(@PathVariable Long id, @RequestParam String status,
			@RequestParam Long approvedBy, @RequestParam(required = false) String remarks) {
		return ApiResponse.success(service.decide(id, status.toUpperCase(), approvedBy, remarks),
				"Leave application updated successfully");
	}
}
