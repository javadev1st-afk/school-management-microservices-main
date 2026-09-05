package com.school.userservice.converter;

import com.school.userservice.dto.StudentDTO;
import com.school.userservice.entity.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StudentConverter {

    public StudentDTO entityToDTO(Student student) {
        if (student == null) {
            return null;
        }
        return StudentDTO.builder()
                .id(student.getId())
                .userId(student.getUserId())
                .rollNumber(student.getRollNumber())
                .classId(student.getClassId())
                .sectionId(student.getSectionId())
                .fatherName(student.getFatherName())
                .motherName(student.getMotherName())
                .dateOfBirth(student.getDateOfBirth())
                .address(student.getAddress())
                .parentPhone(student.getParentPhone())
                .build();
    }

    public Student dtoToEntity(StudentDTO studentDTO) {
        if (studentDTO == null) {
            return null;
        }
        return Student.builder()
                .id(studentDTO.getId())
                .userId(studentDTO.getUserId())
                .rollNumber(studentDTO.getRollNumber())
                .classId(studentDTO.getClassId())
                .sectionId(studentDTO.getSectionId())
                .fatherName(studentDTO.getFatherName())
                .motherName(studentDTO.getMotherName())
                .dateOfBirth(studentDTO.getDateOfBirth())
                .address(studentDTO.getAddress())
                .parentPhone(studentDTO.getParentPhone())
                .build();
    }
}
