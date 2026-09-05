package com.school.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TeacherDTO {
    private Long id;

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotBlank(message = "Employee ID is required")
    private String employeeId;

    private String qualification;
    private String specialization;
    private LocalDate joiningDate;
    private Integer experienceYears;
}
