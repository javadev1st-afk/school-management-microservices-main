package com.school.userservice.repository;

import com.school.userservice.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByUserId(Long userId);
    Optional<Student> findByRollNumber(String rollNumber);
    List<Student> findByClassIdAndSectionId(Long classId, Long sectionId);
    Page<Student> findByClassIdAndSectionId(Long classId, Long sectionId, Pageable pageable);
    List<Student> findByClassId(Long classId);
}
