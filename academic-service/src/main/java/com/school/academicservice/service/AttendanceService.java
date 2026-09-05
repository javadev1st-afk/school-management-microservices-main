package com.school.academicservice.service;

import com.school.academicservice.dto.AttendanceDTO;
import com.school.academicservice.entity.Attendance;
import com.school.academicservice.repository.AttendanceRepository;
import com.school.academicservice.converter.AttendanceConverter;
import com.school.common.exception.ResourceNotFoundException;
import com.school.common.exception.DuplicateResourceException;
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
public class AttendanceService {
    private final AttendanceRepository attendanceRepository;
    private final AttendanceConverter attendanceConverter;

    public AttendanceDTO markAttendance(AttendanceDTO attendanceDTO) {
        log.info("Marking attendance for student: {} on date: {}", attendanceDTO.getStudentId(), attendanceDTO.getAttendanceDate());
        
        if (attendanceRepository.findByStudentIdAndAttendanceDate(attendanceDTO.getStudentId(), attendanceDTO.getAttendanceDate()).isPresent()) {
            throw new DuplicateResourceException("Attendance", "studentId and attendanceDate", attendanceDTO.getStudentId());
        }

        Attendance attendance = attendanceConverter.dtoToEntity(attendanceDTO);
        attendance = attendanceRepository.save(attendance);
        log.info("Attendance marked successfully with id: {}", attendance.getId());
        return attendanceConverter.entityToDTO(attendance);
    }

    public AttendanceDTO getAttendanceById(Long id) {
        log.info("Fetching attendance with id: {}", id);
        Attendance attendance = attendanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Attendance", "id", id));
        return attendanceConverter.entityToDTO(attendance);
    }

    public List<AttendanceDTO> getStudentAttendance(Long studentId) {
        log.info("Fetching attendance for student: {}", studentId);
        List<Attendance> attendances = attendanceRepository.findByStudentId(studentId);
        return attendances.stream()
                .map(attendanceConverter::entityToDTO)
                .collect(Collectors.toList());
    }

    public List<AttendanceDTO> getStudentAttendanceBetweenDates(Long studentId, LocalDate fromDate, LocalDate toDate) {
        log.info("Fetching attendance for student: {} between {} and {}", studentId, fromDate, toDate);
        List<Attendance> attendances = attendanceRepository.findByStudentIdAndAttendanceDateBetween(studentId, fromDate, toDate);
        return attendances.stream()
                .map(attendanceConverter::entityToDTO)
                .collect(Collectors.toList());
    }

    public List<AttendanceDTO> getClassSectionAttendance(Long classId, Long sectionId, LocalDate attendanceDate) {
        log.info("Fetching attendance for class: {} section: {} on date: {}", classId, sectionId, attendanceDate);
        List<Attendance> attendances = attendanceRepository.findByClassIdAndSectionIdAndAttendanceDate(classId, sectionId, attendanceDate);
        return attendances.stream()
                .map(attendanceConverter::entityToDTO)
                .collect(Collectors.toList());
    }

    public AttendanceDTO updateAttendance(Long id, AttendanceDTO attendanceDTO) {
        log.info("Updating attendance with id: {}", id);
        Attendance attendance = attendanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Attendance", "id", id));

        attendance.setStatus(attendanceDTO.getStatus());
        attendance.setRemarks(attendanceDTO.getRemarks());

        attendance = attendanceRepository.save(attendance);
        log.info("Attendance updated successfully with id: {}", attendance.getId());
        return attendanceConverter.entityToDTO(attendance);
    }

    public void deleteAttendance(Long id) {
        log.info("Deleting attendance with id: {}", id);
        if (!attendanceRepository.existsById(id)) {
            throw new ResourceNotFoundException("Attendance", "id", id);
        }
        attendanceRepository.deleteById(id);
        log.info("Attendance deleted successfully with id: {}", id);
    }
}
