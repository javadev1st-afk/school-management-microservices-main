package com.school.communicationservice.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.time.*;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class LeaveApplicationDTO {
    private Long id;
    @NotNull private Long studentId;
    @NotBlank private String reason;
    @NotNull private LocalDate fromDate;
    @NotNull private LocalDate toDate;
    private Integer totalDays;
    private String status;
    private Long approvedBy;
    private LocalDateTime approvalDate;
    private String remarks;
}
