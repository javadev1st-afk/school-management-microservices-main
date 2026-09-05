package com.school.communicationservice.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.*;

@Entity
@Table(name = "leave_applications")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class LeaveApplication {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(name = "student_id", nullable = false) private Long studentId;
    @Column(nullable = false, columnDefinition = "TEXT") private String reason;
    @Column(name = "from_date", nullable = false) private LocalDate fromDate;
    @Column(name = "to_date", nullable = false) private LocalDate toDate;
    @Column(name = "total_days", nullable = false) private Integer totalDays;
    @Column(nullable = false, length = 20) private String status;
    @Column(name = "approved_by") private Long approvedBy;
    @Column(name = "approval_date") private LocalDateTime approvalDate;
    private String remarks;
    @Column(name = "created_at", updatable = false) private LocalDateTime createdAt;
    @Column(name = "updated_at") private LocalDateTime updatedAt;
    @PrePersist void onCreate() { status = status == null ? "PENDING" : status; createdAt = LocalDateTime.now(); updatedAt = createdAt; }
    @PreUpdate void onUpdate() { updatedAt = LocalDateTime.now(); }
}
