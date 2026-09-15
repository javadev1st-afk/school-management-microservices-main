package com.school.academicservice.controller;

import com.school.academicservice.dto.HomeworkDTO;
import com.school.academicservice.service.HomeworkService;
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
@RequestMapping("/api/v1/classwork")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Classwork", description = "Classwork management endpoints")
@SecurityRequirement(name = "bearerAuth")
public class ClassworkController {
    private final HomeworkService homeworkService;

    @PostMapping
    @PreAuthorize("hasRole('TEACHER')")
    @Operation(summary = "Assign new classwork")
    public ResponseEntity<ApiResponse<HomeworkDTO>> createHomework(@Valid @RequestBody HomeworkDTO homeworkDTO) {
        log.info("Create homework request received for class: {}", homeworkDTO.getClassId());
        HomeworkDTO response = homeworkService.createHomework(homeworkDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Homework assigned successfully"));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'STUDENT')")
    @Operation(summary = "Get classwork by ID")
    public ResponseEntity<ApiResponse<HomeworkDTO>> getHomeworkById(@PathVariable Long id) {
        log.info("Get classwork request received for id: {}", id);
        HomeworkDTO response = homeworkService.getHomeworkById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/class/{classId}/section/{sectionName}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'STUDENT')")
    @Operation(summary = "Get classwork by class and section")
    public ResponseEntity<ApiResponse<List<HomeworkDTO>>> getHomeworkByClassAndSection(
            @PathVariable Long classId,
            @PathVariable String sectionName) {
        log.info("Get classwork request for class: {} section: {}", classId, sectionName);
        List<HomeworkDTO> response = homeworkService.getHomeworkByClassAndSection(classId, sectionName);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/teacher/{teacherId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @Operation(summary = "Get classwork assigned by teacher")
    public ResponseEntity<ApiResponse<List<HomeworkDTO>>> getHomeworkByTeacher(@PathVariable Long teacherId) {
        log.info("Get classwork request for teacher: {}", teacherId);
        List<HomeworkDTO> response = homeworkService.getHomeworkByTeacher(teacherId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/upcoming")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'STUDENT')")
    @Operation(summary = "Get upcoming classwork")
    public ResponseEntity<ApiResponse<List<HomeworkDTO>>> getUpcomingHomework(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
        log.info("Get upcoming classwork request between {} and {}", fromDate, toDate);
        List<HomeworkDTO> response = homeworkService.getUpcomingHomework(fromDate, toDate);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    @Operation(summary = "Update classwork")
    public ResponseEntity<ApiResponse<HomeworkDTO>> updateHomework(
            @PathVariable Long id,
            @Valid @RequestBody HomeworkDTO homeworkDTO) {
        log.info("Update classwork request received for id: {}", id);
        HomeworkDTO response = homeworkService.updateHomework(id, homeworkDTO);
        return ResponseEntity.ok(ApiResponse.success(response, "Classwork updated successfully"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    @Operation(summary = "Delete classwork")
    public ResponseEntity<ApiResponse<Void>> deleteHomework(@PathVariable Long id) {
        log.info("Delete classwork request received for id: {}", id);
        homeworkService.deleteHomework(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Classwork deleted successfully"));
    }
}
