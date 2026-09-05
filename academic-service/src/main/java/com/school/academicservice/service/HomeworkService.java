package com.school.academicservice.service;

import com.school.academicservice.dto.HomeworkDTO;
import com.school.academicservice.entity.Homework;
import com.school.academicservice.repository.HomeworkRepository;
import com.school.academicservice.converter.HomeworkConverter;
import com.school.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class HomeworkService {
    private final HomeworkRepository homeworkRepository;
    private final HomeworkConverter homeworkConverter;

    public HomeworkDTO createHomework(HomeworkDTO homeworkDTO) {
        log.info("Creating homework: {} for class: {} section: {}", homeworkDTO.getTitle(), homeworkDTO.getClassId(), homeworkDTO.getSectionId());
        
        Homework homework = homeworkConverter.dtoToEntity(homeworkDTO);
        homework = homeworkRepository.save(homework);
        log.info("Homework created successfully with id: {}", homework.getId());
        return homeworkConverter.entityToDTO(homework);
    }

    public HomeworkDTO getHomeworkById(Long id) {
        log.info("Fetching homework with id: {}", id);
        Homework homework = homeworkRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Homework", "id", id));
        return homeworkConverter.entityToDTO(homework);
    }

    public List<HomeworkDTO> getHomeworkByClassAndSection(Long classId, Long sectionId) {
        log.info("Fetching homework for class: {} section: {}", classId, sectionId);
        List<Homework> homeworks = homeworkRepository.findByClassIdAndSectionId(classId, sectionId);
        return homeworks.stream()
                .map(homeworkConverter::entityToDTO)
                .collect(Collectors.toList());
    }

    public List<HomeworkDTO> getHomeworkByTeacher(Long teacherId) {
        log.info("Fetching homework by teacher: {}", teacherId);
        List<Homework> homeworks = homeworkRepository.findByTeacherId(teacherId);
        return homeworks.stream()
                .map(homeworkConverter::entityToDTO)
                .collect(Collectors.toList());
    }

    public List<HomeworkDTO> getUpcomingHomework(LocalDate fromDate, LocalDate toDate) {
        log.info("Fetching upcoming homework between {} and {}", fromDate, toDate);
        List<Homework> homeworks = homeworkRepository.findByDueDateBetween(fromDate, toDate);
        return homeworks.stream()
                .map(homeworkConverter::entityToDTO)
                .collect(Collectors.toList());
    }

    public HomeworkDTO updateHomework(Long id, HomeworkDTO homeworkDTO) {
        log.info("Updating homework with id: {}", id);
        Homework homework = homeworkRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Homework", "id", id));

        homework.setTitle(homeworkDTO.getTitle());
        homework.setDescription(homeworkDTO.getDescription());
        homework.setFileUrl(homeworkDTO.getFileUrl());
        homework.setDueDate(homeworkDTO.getDueDate());

        homework = homeworkRepository.save(homework);
        log.info("Homework updated successfully with id: {}", homework.getId());
        return homeworkConverter.entityToDTO(homework);
    }

    public void deleteHomework(Long id) {
        log.info("Deleting homework with id: {}", id);
        if (!homeworkRepository.existsById(id)) {
            throw new ResourceNotFoundException("Homework", "id", id);
        }
        homeworkRepository.deleteById(id);
        log.info("Homework deleted successfully with id: {}", id);
    }
}
