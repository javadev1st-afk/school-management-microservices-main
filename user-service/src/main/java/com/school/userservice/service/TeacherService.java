package com.school.userservice.service;

import com.school.userservice.dto.TeacherDTO;
import com.school.userservice.entity.Teacher;
import com.school.userservice.repository.TeacherRepository;
import com.school.userservice.converter.TeacherConverter;
import com.school.common.exception.ResourceNotFoundException;
import com.school.common.exception.DuplicateResourceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class TeacherService {
    private final TeacherRepository teacherRepository;
    private final TeacherConverter teacherConverter;

    public TeacherDTO createTeacher(TeacherDTO teacherDTO) {
        log.info("Creating teacher with employee id: {}", teacherDTO.getEmployeeId());
        
        if (teacherRepository.findByEmployeeId(teacherDTO.getEmployeeId()).isPresent()) {
            throw new DuplicateResourceException("Teacher", "employeeId", teacherDTO.getEmployeeId());
        }

        Teacher teacher = teacherConverter.dtoToEntity(teacherDTO);
        teacher = teacherRepository.save(teacher);
        log.info("Teacher created successfully with id: {}", teacher.getId());
        return teacherConverter.entityToDTO(teacher);
    }

    public TeacherDTO getTeacherById(Long id) {
        log.info("Fetching teacher with id: {}", id);
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher", "id", id));
        return teacherConverter.entityToDTO(teacher);
    }

    public TeacherDTO getTeacherByUserId(Long userId) {
        log.info("Fetching teacher with user id: {}", userId);
        Teacher teacher = teacherRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher", "userId", userId));
        return teacherConverter.entityToDTO(teacher);
    }

    public TeacherDTO getTeacherByEmployeeId(String employeeId) {
        log.info("Fetching teacher with employee id: {}", employeeId);
        Teacher teacher = teacherRepository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher", "employeeId", employeeId));
        return teacherConverter.entityToDTO(teacher);
    }

    public TeacherDTO updateTeacher(Long id, TeacherDTO teacherDTO) {
        log.info("Updating teacher with id: {}", id);
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher", "id", id));

        teacher.setQualification(teacherDTO.getQualification());
        teacher.setSpecialization(teacherDTO.getSpecialization());
        teacher.setJoiningDate(teacherDTO.getJoiningDate());
        teacher.setExperienceYears(teacherDTO.getExperienceYears());

        teacher = teacherRepository.save(teacher);
        log.info("Teacher updated successfully with id: {}", teacher.getId());
        return teacherConverter.entityToDTO(teacher);
    }

    public void deleteTeacher(Long id) {
        log.info("Deleting teacher with id: {}", id);
        if (!teacherRepository.existsById(id)) {
            throw new ResourceNotFoundException("Teacher", "id", id);
        }
        teacherRepository.deleteById(id);
        log.info("Teacher deleted successfully with id: {}", id);
    }

    public List<TeacherDTO> getAllTeachers() {
        log.info("Fetching all teachers");
        List<Teacher> teachers = teacherRepository.findAll();
        return teachers.stream()
                .map(teacherConverter::entityToDTO)
                .collect(Collectors.toList());
    }
}
