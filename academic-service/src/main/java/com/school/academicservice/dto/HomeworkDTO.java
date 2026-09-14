package com.school.academicservice.dto;

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
public class HomeworkDTO {
    private Long id;

    @NotNull(message = "Teacher ID is required")
    private Long teacherId;

    @NotNull(message = "Class ID is required")
    private Long classId;

    @NotNull(message = "Section name is required")
    private String sectionName;

    private Long subjectId;

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    private String fileUrl;

    @NotNull(message = "Due date is required")
    private LocalDate dueDate;

    private String tag;
}
