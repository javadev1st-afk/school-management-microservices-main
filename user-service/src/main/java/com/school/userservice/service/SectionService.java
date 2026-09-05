package com.school.userservice.service;

import com.school.userservice.dto.SectionDTO;
import com.school.userservice.entity.Section;
import com.school.userservice.repository.SectionRepository;
import com.school.userservice.converter.SectionConverter;
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
public class SectionService {
    private final SectionRepository sectionRepository;
    private final SectionConverter sectionConverter;

    public SectionDTO createSection(SectionDTO sectionDTO) {
        log.info("Creating section: {} for class: {}", sectionDTO.getSectionName(), sectionDTO.getClassId());
        
        if (sectionRepository.findByClassIdAndSectionName(sectionDTO.getClassId(), sectionDTO.getSectionName()).isPresent()) {
            throw new DuplicateResourceException("Section", "sectionName", sectionDTO.getSectionName());
        }

        Section section = sectionConverter.dtoToEntity(sectionDTO);
        section = sectionRepository.save(section);
        log.info("Section created successfully with id: {}", section.getId());
        return sectionConverter.entityToDTO(section);
    }

    public SectionDTO getSectionById(Long id) {
        log.info("Fetching section with id: {}", id);
        Section section = sectionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Section", "id", id));
        return sectionConverter.entityToDTO(section);
    }

    public List<SectionDTO> getSectionsByClassId(Long classId) {
        log.info("Fetching sections for class: {}", classId);
        List<Section> sections = sectionRepository.findByClassId(classId);
        return sections.stream()
                .map(sectionConverter::entityToDTO)
                .collect(Collectors.toList());
    }

    public List<SectionDTO> getActiveSectionsByClassId(Long classId) {
        log.info("Fetching active sections for class: {}", classId);
        List<Section> sections = sectionRepository.findByClassIdAndIsActiveTrue(classId);
        return sections.stream()
                .map(sectionConverter::entityToDTO)
                .collect(Collectors.toList());
    }

    public SectionDTO updateSection(Long id, SectionDTO sectionDTO) {
        log.info("Updating section with id: {}", id);
        Section section = sectionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Section", "id", id));

        section.setSectionName(sectionDTO.getSectionName());
        section.setCapacity(sectionDTO.getCapacity());
        section.setIsActive(sectionDTO.getIsActive());

        section = sectionRepository.save(section);
        log.info("Section updated successfully with id: {}", section.getId());
        return sectionConverter.entityToDTO(section);
    }

    public void deleteSection(Long id) {
        log.info("Deleting section with id: {}", id);
        if (!sectionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Section", "id", id);
        }
        sectionRepository.deleteById(id);
        log.info("Section deleted successfully with id: {}", id);
    }

    public List<SectionDTO> getAllSections() {
        log.info("Fetching all sections");
        List<Section> sections = sectionRepository.findAll();
        return sections.stream()
                .map(sectionConverter::entityToDTO)
                .collect(Collectors.toList());
    }
}
