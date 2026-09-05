package com.school.userservice.service;

import com.school.userservice.dto.StudentDTO;
import com.school.userservice.entity.Student;
import com.school.userservice.repository.StudentRepository;
import com.school.userservice.converter.StudentConverter;
import com.school.common.exception.ResourceNotFoundException;
import com.school.common.exception.DuplicateResourceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class StudentService {
    private final StudentRepository studentRepository;
    private final StudentConverter studentConverter;

    public StudentDTO createStudent(StudentDTO studentDTO) {
        log.info("Creating student with roll number: {}", studentDTO.getRollNumber());
        
        if (studentRepository.findByRollNumber(studentDTO.getRollNumber()).isPresent()) {
            throw new DuplicateResourceException("Student", "rollNumber", studentDTO.getRollNumber());
        }

        Student student = studentConverter.dtoToEntity(studentDTO);
        student = studentRepository.save(student);
        log.info("Student created successfully with id: {}", student.getId());
        return studentConverter.entityToDTO(student);
    }

    public StudentDTO getStudentById(Long id) {
        log.info("Fetching student with id: {}", id);
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", id));
        return studentConverter.entityToDTO(student);
    }

    public StudentDTO getStudentByUserId(Long userId) {
        log.info("Fetching student with user id: {}", userId);
        Student student = studentRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "userId", userId));
        return studentConverter.entityToDTO(student);
    }

    public StudentDTO getStudentByRollNumber(String rollNumber) {
        log.info("Fetching student with roll number: {}", rollNumber);
        Student student = studentRepository.findByRollNumber(rollNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "rollNumber", rollNumber));
        return studentConverter.entityToDTO(student);
    }

    public List<StudentDTO> getStudentsByClassAndSection(Long classId, Long sectionId) {
        log.info("Fetching students for class: {} and section: {}", classId, sectionId);
        List<Student> students = studentRepository.findByClassIdAndSectionId(classId, sectionId);
        return students.stream()
                .map(studentConverter::entityToDTO)
                .collect(Collectors.toList());
    }

    public Page<StudentDTO> getStudentsByClassAndSection(Long classId, Long sectionId, Pageable pageable) {
        log.info("Fetching paginated students for class: {} and section: {}", classId, sectionId);
        Page<Student> students = studentRepository.findByClassIdAndSectionId(classId, sectionId, pageable);
        return students.map(studentConverter::entityToDTO);
    }

    public StudentDTO updateStudent(Long id, StudentDTO studentDTO) {
        log.info("Updating student with id: {}", id);
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", id));

        student.setFatherName(studentDTO.getFatherName());
        student.setMotherName(studentDTO.getMotherName());
        student.setDateOfBirth(studentDTO.getDateOfBirth());
        student.setAddress(studentDTO.getAddress());
        student.setParentPhone(studentDTO.getParentPhone());

        student = studentRepository.save(student);
        log.info("Student updated successfully with id: {}", student.getId());
        return studentConverter.entityToDTO(student);
    }

    public void deleteStudent(Long id) {
        log.info("Deleting student with id: {}", id);
        if (!studentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Student", "id", id);
        }
        studentRepository.deleteById(id);
        log.info("Student deleted successfully with id: {}", id);
    }

    public List<StudentDTO> getAllStudents() {
        log.info("Fetching all students");
        List<Student> students = studentRepository.findAll();
        return students.stream()
                .map(studentConverter::entityToDTO)
                .collect(Collectors.toList());
    }
}
