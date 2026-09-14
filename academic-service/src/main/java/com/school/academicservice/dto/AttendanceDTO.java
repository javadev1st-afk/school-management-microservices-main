package com.school.academicservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AttendanceDTO {
    private Long id;

    @NotNull(message = "Admission number is required")
    private Long admissionNumber;

    @NotNull(message = "Teacher ID is required")
    private Long teacherId;

    @NotNull(message = "Class ID is required")
    private Long classId;

    @NotNull(message = "Section name is required")
    private String sectionName;

    @NotNull(message = "Attendance date is required")
    private LocalDate attendanceDate;

    @NotNull(message = "Attendance status is required")
    private String status; // PRESENT, ABSENT, LEAVE, SICK_LEAVE

    private String remarks;
}
