package com.school.academicservice.service;

import com.school.academicservice.dto.HomeworkDTO;
import com.school.academicservice.entity.Homework;
import com.school.academicservice.entity.HomeworkFile;
import com.school.academicservice.dto.HomeworkFileDTO;
import com.school.academicservice.repository.HomeworkRepository;
import com.school.academicservice.repository.HomeworkFileRepository;
import com.school.academicservice.converter.HomeworkConverter;
import com.school.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
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
    private final HomeworkFileRepository homeworkFileRepository;

    public HomeworkDTO createHomework(HomeworkDTO homeworkDTO) {
        log.info("Creating homework: {} for class: {} section: {}", homeworkDTO.getTitle(), homeworkDTO.getClassId(), homeworkDTO.getSectionName());
        
        Homework homework = homeworkConverter.dtoToEntity(homeworkDTO);
        homework = homeworkRepository.save(homework);
        log.info("Homework created successfully with id: {}", homework.getId());
        return homeworkConverter.entityToDTO(homework);
    }

    public HomeworkDTO createHomework(HomeworkDTO homeworkDTO, List<MultipartFile> files) throws IOException {
        Homework homework = homeworkConverter.dtoToEntity(homeworkDTO);
        addFiles(homework, files);
        return homeworkConverter.entityToDTO(homeworkRepository.save(homework));
    }

    public HomeworkDTO getHomeworkById(Long id) {
        log.info("Fetching homework with id: {}", id);
        Homework homework = homeworkRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Homework", "id", id));
        return homeworkConverter.entityToDTO(homework);
    }

    public List<HomeworkDTO> getHomeworkByClassAndSection(Long classId, String sectionName) {
        log.info("Fetching homework for class: {} section: {}", classId, sectionName);
        List<Homework> homeworks = homeworkRepository.findByClassIdAndSectionName(classId, sectionName);
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

    public HomeworkDTO updateHomework(Long id, HomeworkDTO homeworkDTO, List<MultipartFile> files) throws IOException {
        Homework homework = homeworkRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Homework", "id", id));
        homework.setTitle(homeworkDTO.getTitle());
        homework.setDescription(homeworkDTO.getDescription());
        homework.setFileUrl(homeworkDTO.getFileUrl());
        homework.setDueDate(homeworkDTO.getDueDate());
        if (files != null && !files.isEmpty()) {
            homework.getFiles().clear();
            addFiles(homework, files);
        }
        return homeworkConverter.entityToDTO(homeworkRepository.save(homework));
    }

    @Transactional(readOnly = true)
    public HomeworkFileDTO getFile(Long fileId) {
        HomeworkFile file = homeworkFileRepository.findById(fileId)
                .orElseThrow(() -> new ResourceNotFoundException("Homework file", "id", fileId));
        return HomeworkFileDTO.builder()
                .id(file.getId())
                .fileName(file.getFileName())
                .contentType(file.getContentType())
                .fileSize(file.getFileSize())
                .fileData(file.getFileData())
                .build();
    }

    private void addFiles(Homework homework, List<MultipartFile> files) throws IOException {
        if (files == null) {
            return;
        }
        for (MultipartFile file : files) {
            if (file == null || file.isEmpty()) {
                throw new IllegalArgumentException("Uploaded files must not be empty");
            }
            homework.getFiles().add(HomeworkFile.builder()
                    .homework(homework)
                    .fileName(file.getOriginalFilename() == null ? "file" : file.getOriginalFilename())
                    .contentType(file.getContentType())
                    .fileSize(file.getSize())
                    .fileData(file.getBytes())
                    .build());
        }
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
