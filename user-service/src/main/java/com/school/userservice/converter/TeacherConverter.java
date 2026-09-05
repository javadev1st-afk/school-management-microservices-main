package com.school.userservice.converter;

import com.school.userservice.dto.TeacherDTO;
import com.school.userservice.entity.Teacher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TeacherConverter {

    public TeacherDTO entityToDTO(Teacher teacher) {
        if (teacher == null) {
            return null;
        }
        return TeacherDTO.builder()
                .id(teacher.getId())
                .userId(teacher.getUserId())
                .employeeId(teacher.getEmployeeId())
                .qualification(teacher.getQualification())
                .specialization(teacher.getSpecialization())
                .joiningDate(teacher.getJoiningDate())
                .experienceYears(teacher.getExperienceYears())
                .build();
    }

    public Teacher dtoToEntity(TeacherDTO teacherDTO) {
        if (teacherDTO == null) {
            return null;
        }
        return Teacher.builder()
                .id(teacherDTO.getId())
                .userId(teacherDTO.getUserId())
                .employeeId(teacherDTO.getEmployeeId())
                .qualification(teacherDTO.getQualification())
                .specialization(teacherDTO.getSpecialization())
                .joiningDate(teacherDTO.getJoiningDate())
                .experienceYears(teacherDTO.getExperienceYears())
                .build();
    }
}
