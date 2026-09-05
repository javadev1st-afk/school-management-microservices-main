package com.school.academicservice.converter;

import com.school.academicservice.dto.AttendanceDTO;
import com.school.academicservice.entity.Attendance;
import org.springframework.stereotype.Component;

@Component
public class AttendanceConverter {

    public AttendanceDTO entityToDTO(Attendance attendance) {
        if (attendance == null) {
            return null;
        }
        return AttendanceDTO.builder()
                .id(attendance.getId())
                .studentId(attendance.getStudentId())
                .teacherId(attendance.getTeacherId())
                .classId(attendance.getClassId())
                .sectionId(attendance.getSectionId())
                .attendanceDate(attendance.getAttendanceDate())
                .status(attendance.getStatus())
                .remarks(attendance.getRemarks())
                .build();
    }

    public Attendance dtoToEntity(AttendanceDTO attendanceDTO) {
        if (attendanceDTO == null) {
            return null;
        }
        return Attendance.builder()
                .id(attendanceDTO.getId())
                .studentId(attendanceDTO.getStudentId())
                .teacherId(attendanceDTO.getTeacherId())
                .classId(attendanceDTO.getClassId())
                .sectionId(attendanceDTO.getSectionId())
                .attendanceDate(attendanceDTO.getAttendanceDate())
                .status(attendanceDTO.getStatus())
                .remarks(attendanceDTO.getRemarks())
                .build();
    }
}
