package com.school.academicservice.repository;

import com.school.academicservice.entity.ExamSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExamScheduleRepository extends JpaRepository<ExamSchedule, Long> {
    List<ExamSchedule> findByClassIdAndSectionId(Long classId, Long sectionId);
    List<ExamSchedule> findByExamDateBetween(LocalDate fromDate, LocalDate toDate);
    List<ExamSchedule> findByClassIdAndSectionIdAndSubjectId(Long classId, Long sectionId, Long subjectId);
}
