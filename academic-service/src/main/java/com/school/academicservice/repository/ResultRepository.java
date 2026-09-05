package com.school.academicservice.repository;

import com.school.academicservice.entity.Result;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ResultRepository extends JpaRepository<Result, Long> {
    Optional<Result> findByStudentIdAndExamScheduleIdAndSubjectId(Long studentId, Long examScheduleId, Long subjectId);
    List<Result> findByStudentId(Long studentId);
    List<Result> findByExamScheduleId(Long examScheduleId);
}
