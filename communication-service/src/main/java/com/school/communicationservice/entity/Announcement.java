package com.school.communicationservice.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "announcements")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Announcement {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "created_by", nullable = false) private Long createdBy;
    @Column(nullable = false) private String title;
    @Column(nullable = false, columnDefinition = "TEXT") private String content;
    @Column(name = "file_url", length = 500) private String fileUrl;
    @Column(name = "class_id") private Long classId;
    @Column(name = "section_id") private Long sectionId;
    @Column(name = "posted_date") private LocalDateTime postedDate;
    @Column(name = "expires_date") private LocalDateTime expiresDate;
    @Column(name = "is_active") private Boolean active;
    @Column(name = "created_at", updatable = false) private LocalDateTime createdAt;
    @Column(name = "updated_at") private LocalDateTime updatedAt;
    @PrePersist void onCreate() { postedDate = postedDate == null ? LocalDateTime.now() : postedDate; active = active == null || active; createdAt = LocalDateTime.now(); updatedAt = createdAt; }
    @PreUpdate void onUpdate() { updatedAt = LocalDateTime.now(); }
}
