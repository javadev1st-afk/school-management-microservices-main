package com.school.communicationservice.repository;

import com.school.communicationservice.entity.LeaveApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LeaveApplicationRepository extends JpaRepository<LeaveApplication, Long> {
    List<LeaveApplication> findByStudentIdOrderByCreatedAtDesc(Long studentId);
    List<LeaveApplication> findByStatusOrderByCreatedAtAsc(String status);
}
