package com.programming.management_service.mapper;

import com.programming.common.common_dto.transition.StudentEndYearStatusDto;
import com.programming.management_service.domain.dto.response.transition.TransitionResultResponseDto;
import com.programming.management_service.domain.dto.response.transition.TransitionStagingResponseDto;
import com.programming.management_service.domain.model.Classroom;
import com.programming.management_service.domain.model.Enrollment;
import com.programming.management_service.domain.model.TransitionStaging;
import com.programming.management_service.repository.ClassroomRepository;
import com.programming.management_service.management_caller.UserClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class TransitionMapper {
    
    private final ClassroomRepository classroomRepository;
    private final UserClient userClient;

    public TransitionStagingResponseDto toStagingResponseDto(TransitionStaging staging) {
        String oldClassroomName = getClassroomName(staging.getOldClassroomId());
        String newClassroomName = getClassroomName(staging.getNewClassroomId());
        String studentName = getStudentName(staging.getStudentId());
        
        return TransitionStagingResponseDto.builder()
                .id(staging.getId())
                .studentId(staging.getStudentId())
                .studentName(studentName)
                .oldClassroomId(staging.getOldClassroomId())
                .oldClassroomName(oldClassroomName)
                .oldAcademicYear(staging.getOldAcademicYear())
                .oldStudentCode(staging.getOldStudentCode())
                .oldStatus(staging.getOldStatus().name())
                .newClassroomId(staging.getNewClassroomId())
                .newClassroomName(newClassroomName)
                .newAcademicYear(staging.getNewAcademicYear())
                .newStudentCode(staging.getNewStudentCode())
                .newEnrollmentId(staging.getNewEnrollmentId())
                .note(staging.getNote())
                .isFinalized(staging.getIsFinalized())
                .createdAt(staging.getCreatedAt())
                .finalizedAt(staging.getFinalizedAt())
                .build();
    }

    public StudentEndYearStatusDto toStudentEndYearStatusDto(Enrollment enrollment) {
        String studentName = getStudentName(enrollment.getStudentId());
        
        return StudentEndYearStatusDto.builder()
                .enrollmentId(enrollment.getId())
                .studentId(enrollment.getStudentId())
                .studentName(studentName)
                .studentCode(enrollment.getStudentCode())
                .currentStatus(enrollment.getStatus().name())
                .note(enrollment.getNote())
                .build();
    }

    public TransitionResultResponseDto.NewEnrollmentInfo toNewEnrollmentInfo(
            TransitionStaging staging) {
        
        String newClassroomName = getClassroomName(staging.getNewClassroomId());
        String studentName = getStudentName(staging.getStudentId());
        
        return TransitionResultResponseDto.NewEnrollmentInfo.builder()
                .studentId(staging.getStudentId())
                .studentName(studentName)
                .oldCode(staging.getOldStudentCode())
                .newCode(staging.getNewStudentCode())
                .newEnrollmentId(staging.getNewEnrollmentId())
                .newClassroom(newClassroomName)
                .academicYear(staging.getNewAcademicYear())
                .build();
    }

    private String getClassroomName(Long classroomId) {
        return classroomRepository.findById(classroomId)
                .map(Classroom::getName)
                .orElse("Unknown");
    }
    
    private String getStudentName(Long studentId) {
        try {
            return userClient.getUserById(studentId).getFullName();
        } catch (Exception e) {
            log.warn("Failed to fetch student name for studentId: {}. Error: {}", 
                    studentId, e.getMessage());
            return "Student " + studentId;
        }
    }
}
