package com.school.userservice.controller;

import com.school.userservice.dto.StudentDTO;
import com.school.userservice.service.StudentService;
import com.school.common.response.ApiResponse;
import com.school.common.dto.PageResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/students")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Students", description = "Student management endpoints")
@SecurityRequirement(name = "bearerAuth")
public class StudentController {
    private final StudentService studentService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @Operation(summary = "Create a new student")
    public ResponseEntity<ApiResponse<StudentDTO>> createStudent(@Valid @RequestBody StudentDTO studentDTO) {
        log.info("Create student request received for roll number: {}", studentDTO.getRollNumber());
        StudentDTO response = studentService.createStudent(studentDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Student created successfully"));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'STUDENT')")
    @Operation(summary = "Get student by ID")
    public ResponseEntity<ApiResponse<StudentDTO>> getStudentById(@PathVariable Long id) {
        log.info("Get student request received for id: {}", id);
        StudentDTO response = studentService.getStudentById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'STUDENT')")
    @Operation(summary = "Get student by user ID")
    public ResponseEntity<ApiResponse<StudentDTO>> getStudentByUserId(@PathVariable Long userId) {
        log.info("Get student request received for user id: {}", userId);
        StudentDTO response = studentService.getStudentByUserId(userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/class/{classId}/section/{sectionId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'STUDENT')")
    @Operation(summary = "Get all students by class and section")
    public ResponseEntity<ApiResponse<List<StudentDTO>>> getStudentsByClassAndSection(
            @PathVariable Long classId,
            @PathVariable Long sectionId) {
        log.info("Get students request received for class: {} and section: {}", classId, sectionId);
        List<StudentDTO> response = studentService.getStudentsByClassAndSection(classId, sectionId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/class/{classId}/section/{sectionId}/paginated")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @Operation(summary = "Get paginated students by class and section")
    public ResponseEntity<ApiResponse<Page<StudentDTO>>> getStudentsByClassAndSectionPaginated(
            @PathVariable Long classId,
            @PathVariable Long sectionId,
            Pageable pageable) {
        log.info("Get paginated students request received for class: {} and section: {}", classId, sectionId);
        Page<StudentDTO> response = studentService.getStudentsByClassAndSection(classId, sectionId, pageable);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT')")
    @Operation(summary = "Update student")
    public ResponseEntity<ApiResponse<StudentDTO>> updateStudent(
            @PathVariable Long id,
            @Valid @RequestBody StudentDTO studentDTO) {
        log.info("Update student request received for id: {}", id);
        StudentDTO response = studentService.updateStudent(id, studentDTO);
        return ResponseEntity.ok(ApiResponse.success(response, "Student updated successfully"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete student")
    public ResponseEntity<ApiResponse<Void>> deleteStudent(@PathVariable Long id) {
        log.info("Delete student request received for id: {}", id);
        studentService.deleteStudent(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Student deleted successfully"));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all students")
    public ResponseEntity<ApiResponse<List<StudentDTO>>> getAllStudents() {
        log.info("Get all students request received");
        List<StudentDTO> response = studentService.getAllStudents();
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
