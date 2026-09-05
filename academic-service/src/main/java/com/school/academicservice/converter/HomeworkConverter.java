package com.school.academicservice.converter;

import com.school.academicservice.dto.HomeworkDTO;
import com.school.academicservice.entity.Homework;
import org.springframework.stereotype.Component;

@Component
public class HomeworkConverter {

    public HomeworkDTO entityToDTO(Homework homework) {
        if (homework == null) {
            return null;
        }
        return HomeworkDTO.builder()
                .id(homework.getId())
                .teacherId(homework.getTeacherId())
                .classId(homework.getClassId())
                .sectionId(homework.getSectionId())
                .subjectId(homework.getSubjectId())
                .title(homework.getTitle())
                .description(homework.getDescription())
                .fileUrl(homework.getFileUrl())
                .dueDate(homework.getDueDate())
                .build();
    }

    public Homework dtoToEntity(HomeworkDTO homeworkDTO) {
        if (homeworkDTO == null) {
            return null;
        }
        return Homework.builder()
                .id(homeworkDTO.getId())
                .teacherId(homeworkDTO.getTeacherId())
                .classId(homeworkDTO.getClassId())
                .sectionId(homeworkDTO.getSectionId())
                .subjectId(homeworkDTO.getSubjectId())
                .title(homeworkDTO.getTitle())
                .description(homeworkDTO.getDescription())
                .fileUrl(homeworkDTO.getFileUrl())
                .dueDate(homeworkDTO.getDueDate())
                .build();
    }
}
