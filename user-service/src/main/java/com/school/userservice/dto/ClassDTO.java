package com.school.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClassDTO {
    private Long id;

    @NotBlank(message = "Class name is required")
    private String className;

    @NotBlank(message = "Academic year is required")
    private String academicYear;

    private Boolean isActive;
}
