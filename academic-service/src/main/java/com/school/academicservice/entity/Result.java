package com.school.academicservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "results")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Result {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "student_id", nullable = false)
    private Long studentId;

    @Column(name = "exam_schedule_id", nullable = false)
    private Long examScheduleId;

    @Column(name = "subject_id", nullable = false)
    private Long subjectId;

    @Column(name = "marks_obtained", nullable = false)
    private Integer marksObtained;

    @Column(name = "out_of", nullable = false)
    private Integer outOf = 100;

    @Column(length = 10)
    private String grade;

    @Column(name = "file_url")
    private String fileUrl;

    @Column(name = "published_date")
    private LocalDateTime publishedDate;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
