package com.school.communicationservice.service;

import com.school.communicationservice.converter.LeaveApplicationConverter;
import com.school.communicationservice.dto.LeaveApplicationDTO;
import com.school.communicationservice.entity.LeaveApplication;
import com.school.communicationservice.repository.LeaveApplicationRepository;
import com.school.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class LeaveApplicationService {
	private final LeaveApplicationRepository repository;
	private final LeaveApplicationConverter converter;

	public LeaveApplicationDTO apply(LeaveApplicationDTO dto) {
		validateDates(dto);
		dto.setTotalDays((int) (dto.getToDate().toEpochDay() - dto.getFromDate().toEpochDay() + 1));
		dto.setStatus("PENDING");
		return converter.toDto(repository.save(converter.toEntity(dto)));
	}

	@Transactional(readOnly = true)
	public LeaveApplicationDTO get(Long id) {
		return converter.toDto(find(id));
	}

	@Transactional(readOnly = true)
	public List<LeaveApplicationDTO> byStudent(Long studentId) {
		return repository.findByAdmissionNumberOrderByCreatedAtDesc(studentId).stream().map(converter::toDto).toList();
	}

	@Transactional(readOnly = true)
	public List<LeaveApplicationDTO> byStatus(String status) {
		return repository.findByStatusOrderByCreatedAtAsc(status).stream().map(converter::toDto).toList();
	}

	public LeaveApplicationDTO decide(Long id, String status, Long approvedBy, String remarks) {
		LeaveApplication e = find(id);
		e.setStatus(status);
		e.setApprovedBy(approvedBy);
		e.setApprovalDate(LocalDateTime.now());
		e.setRemarks(remarks);
		return converter.toDto(repository.save(e));
	}

	private void validateDates(LeaveApplicationDTO dto) {
		if (dto.getToDate().isBefore(dto.getFromDate()))
			throw new IllegalArgumentException("toDate must not be before fromDate");
	}

	private LeaveApplication find(Long id) {
		return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Leave application", "id", id));
	}
}
