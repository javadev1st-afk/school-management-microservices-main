package com.school.academicservice.controller;

import com.school.academicservice.dto.AttendanceDTO;
import com.school.academicservice.service.AttendanceService;
import com.school.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/attendance")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Attendance", description = "Attendance management endpoints")
@SecurityRequirement(name = "bearerAuth")
public class AttendanceController {
    private final AttendanceService attendanceService;

    @PostMapping("/mark")
    @PreAuthorize("hasRole('TEACHER')")
    @Operation(summary = "Mark attendance")
    public ResponseEntity<ApiResponse<AttendanceDTO>> markAttendance(@Valid @RequestBody AttendanceDTO attendanceDTO) {
        log.info("Mark attendance request received for student: {}", attendanceDTO.getStudentId());
        AttendanceDTO response = attendanceService.markAttendance(attendanceDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Attendance marked successfully"));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'STUDENT')")
    @Operation(summary = "Get attendance by ID")
    public ResponseEntity<ApiResponse<AttendanceDTO>> getAttendanceById(@PathVariable Long id) {
        log.info("Get attendance request received for id: {}", id);
        AttendanceDTO response = attendanceService.getAttendanceById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'STUDENT')")
    @Operation(summary = "Get all attendance for a student")
    public ResponseEntity<ApiResponse<List<AttendanceDTO>>> getStudentAttendance(@PathVariable Long studentId) {
        log.info("Get student attendance request received for student: {}", studentId);
        List<AttendanceDTO> response = attendanceService.getStudentAttendance(studentId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/student/{studentId}/date-range")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'STUDENT')")
    @Operation(summary = "Get attendance between dates")
    public ResponseEntity<ApiResponse<List<AttendanceDTO>>> getStudentAttendanceDateRange(
            @PathVariable Long studentId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
        log.info("Get student attendance request between {} and {}", fromDate, toDate);
        List<AttendanceDTO> response = attendanceService.getStudentAttendanceBetweenDates(studentId, fromDate, toDate);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/class/{classId}/section/{sectionId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @Operation(summary = "Get class/section attendance")
    public ResponseEntity<ApiResponse<List<AttendanceDTO>>> getClassSectionAttendance(
            @PathVariable Long classId,
            @PathVariable Long sectionId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("Get class section attendance request for class: {} section: {}", classId, sectionId);
        List<AttendanceDTO> response = attendanceService.getClassSectionAttendance(classId, sectionId, date);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    @Operation(summary = "Update attendance")
    public ResponseEntity<ApiResponse<AttendanceDTO>> updateAttendance(
            @PathVariable Long id,
            @Valid @RequestBody AttendanceDTO attendanceDTO) {
        log.info("Update attendance request received for id: {}", id);
        AttendanceDTO response = attendanceService.updateAttendance(id, attendanceDTO);
        return ResponseEntity.ok(ApiResponse.success(response, "Attendance updated successfully"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete attendance")
    public ResponseEntity<ApiResponse<Void>> deleteAttendance(@PathVariable Long id) {
        log.info("Delete attendance request received for id: {}", id);
        attendanceService.deleteAttendance(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Attendance deleted successfully"));
    }
}
