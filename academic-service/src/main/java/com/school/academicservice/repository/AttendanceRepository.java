package com.school.academicservice.repository;

import com.school.academicservice.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    Optional<Attendance> findByStudentIdAndAttendanceDate(Long studentId, LocalDate attendanceDate);
    List<Attendance> findByStudentId(Long studentId);
    List<Attendance> findByClassIdAndSectionIdAndAttendanceDate(Long classId, Long sectionId, LocalDate attendanceDate);
    List<Attendance> findByStudentIdAndAttendanceDateBetween(Long studentId, LocalDate fromDate, LocalDate toDate);
}
