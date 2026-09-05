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
public class StudentDTO {
    private Long id;

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotBlank(message = "Roll number is required")
    private String rollNumber;

    @NotNull(message = "Class ID is required")
    private Long classId;

    @NotNull(message = "Section ID is required")
    private Long sectionId;

    private String fatherName;
    private String motherName;
    private LocalDate dateOfBirth;
    private String address;
    private String parentPhone;
}
