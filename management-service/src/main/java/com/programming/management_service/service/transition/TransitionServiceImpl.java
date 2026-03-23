package com.programming.management_service.service.transition;

import com.programming.common.common_dto.transition.EndYearReviewDto;
import com.programming.common.common_dto.transition.StudentEndYearStatusDto;
import com.programming.common.exception.AlreadyExistsException;
import com.programming.common.exception.ResourceNotFoundException;
import com.programming.management_service.domain.dto.request.transition.FinalizeAllTransitionsRequest;
import com.programming.management_service.domain.dto.request.transition.FinalizeTransitionRequest;
import com.programming.management_service.domain.dto.request.transition.GenerateTransitionPlanRequest;
import com.programming.management_service.domain.dto.request.transition.UpdateTransitionStagingRequest;
import com.programming.management_service.domain.dto.response.transition.FinalizeAllTransitionsResponseDto;
import com.programming.management_service.domain.dto.response.transition.TransitionPlanResponseDto;
import com.programming.management_service.domain.dto.response.transition.TransitionResultResponseDto;
import com.programming.management_service.domain.dto.response.transition.TransitionStagingResponseDto;
import com.programming.management_service.domain.model.Classroom;
import com.programming.management_service.domain.model.Enrollment;
import com.programming.management_service.domain.model.EnrollmentStatus;
import com.programming.management_service.domain.model.TransitionStaging;
import com.programming.management_service.repository.ClassroomRepository;
import com.programming.management_service.repository.EnrollmentRepository;
import com.programming.management_service.repository.TransitionStagingRepository;
import com.programming.management_service.service.code.BatchStudentCodeService;
import com.programming.management_service.service.enrollment.EnrollmentService;
import com.programming.management_service.mapper.TransitionMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransitionServiceImpl implements TransitionService {
    
    private final TransitionStagingRepository transitionStagingRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final ClassroomRepository classroomRepository;
    private final BatchStudentCodeService batchStudentCodeService;
    private final EnrollmentService enrollmentService;
    private final TransitionMapper transitionMapper;

    @Override
    @Transactional(readOnly = true)
    public EndYearReviewDto getEndYearReview(Long classroomId, String academicYear) {
        log.info("Getting end-of-year review for classroomId: {}, year: {}", classroomId, academicYear);
        
        Classroom classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new ResourceNotFoundException("Classroom not found with id: " + classroomId));
        
        List<Enrollment> enrollments = enrollmentRepository
                .findByClassroomIdAndAcademicYear(classroomId, academicYear);
        
        if (enrollments.isEmpty()) {
            log.warn("No enrollments found for classroomId: {}, year: {}", classroomId, academicYear);
            return EndYearReviewDto.builder()
                    .classroomId(classroomId)
                    .classroomName(classroom.getName())
                    .academicYear(academicYear)
                    .totalStudents(0)
                    .completedCount(0)
                    .retainedCount(0)
                    .droppedCount(0)
                    .students(new ArrayList<>())
                    .build();
        }
        
        int completedCount = 0;
        int retainedCount = 0;
        int droppedCount = 0;
        
        List<StudentEndYearStatusDto> studentList = new ArrayList<>();
        
        for (Enrollment enrollment : enrollments) {
            // count by status
            if (enrollment.getStatus() == EnrollmentStatus.COMPLETED) {
                completedCount++;
            } else if (enrollment.getStatus() == EnrollmentStatus.RETAINED) {
                retainedCount++;
            } else if (enrollment.getStatus() == EnrollmentStatus.DROPPED) {
                droppedCount++;
            }
            studentList.add(transitionMapper.toStudentEndYearStatusDto(enrollment));
        }
        
        EndYearReviewDto response = EndYearReviewDto.builder()
                .classroomId(classroomId)
                .classroomName(classroom.getName())
                .academicYear(academicYear)
                .totalStudents(enrollments.size())
                .completedCount(completedCount)
                .retainedCount(retainedCount)
                .droppedCount(droppedCount)
                .students(studentList)
                .build();
        
        log.info("End-of-year review: {} students ({} completed, {} retained, {} dropped)", 
                enrollments.size(), completedCount, retainedCount, droppedCount);
        
        return response;
    }

    @Override
    @Transactional
    public TransitionPlanResponseDto generateTransitionPlan(GenerateTransitionPlanRequest request) {
        log.info("Generating transition plan for classroomId: {}, from: {} to: {}", 
                request.getClassroomId(), request.getFromAcademicYear(), request.getToAcademicYear());
        
        Classroom oldClassroom = classroomRepository.findById(request.getClassroomId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Classroom not found with id: " + request.getClassroomId()));
        
        List<TransitionStaging> existingPending = transitionStagingRepository
                .findByOldClassroomIdAndOldAcademicYearAndIsFinalized(
                        request.getClassroomId(), 
                        request.getFromAcademicYear(), 
                        false);
        
        if (!existingPending.isEmpty()) {
            if (request.getResetExisting()) {
                log.info("Deleting {} existing pending transitions", existingPending.size());
                transitionStagingRepository.deleteAll(existingPending);
            } else {
                throw new AlreadyExistsException(
                        "Pending transitions already exist for this classroom. Set resetExisting=true to overwrite.");
            }
        }
        
        List<Enrollment> enrollments = enrollmentRepository
                .findByClassroomIdAndAcademicYear(request.getClassroomId(), request.getFromAcademicYear());
        
        List<Enrollment> transitionableEnrollments = enrollments.stream()
                .filter(e -> e.getStatus() == EnrollmentStatus.COMPLETED || 
                           e.getStatus() == EnrollmentStatus.RETAINED)
                .collect(Collectors.toList());
        
        if (transitionableEnrollments.isEmpty()) {
            throw new IllegalStateException(
                    "No students with COMPLETED or RETAINED status found. Please update enrollment status first.");
        }
        
        // next classroom for COMPLETED students
        Classroom nextClassroom = null;
        if (oldClassroom.getNextClassroomName() != null && !oldClassroom.getNextClassroomName().trim().isEmpty()) {
            List<Classroom> nextClassrooms = classroomRepository
                    .findByNameAndAcademicYear(oldClassroom.getNextClassroomName(), request.getToAcademicYear());
            
            if (nextClassrooms.isEmpty()) {
                log.warn("Next classroom '{}' for year '{}' not found. Need to create it first.", 
                        oldClassroom.getNextClassroomName(), request.getToAcademicYear());
                throw new ResourceNotFoundException(
                        "Next classroom '" + oldClassroom.getNextClassroomName() + 
                        "' for year '" + request.getToAcademicYear() + "' not found. Please create it first.");
            }
            nextClassroom = nextClassrooms.get(0);
        }
        
        // next classroom for RETAINED students
        List<Classroom> sameClassroomNewYear = classroomRepository
                .findByNameAndAcademicYear(oldClassroom.getName(), request.getToAcademicYear());
        
        if (sameClassroomNewYear.isEmpty()) {
            throw new ResourceNotFoundException(
                    "Classroom '" + oldClassroom.getName() + "' for new year '" + 
                    request.getToAcademicYear() + "' not found. Please create it first.");
        }
        Classroom sameClassroom = sameClassroomNewYear.get(0);
        
        List<TransitionStaging> stagingRecords = new ArrayList<>();
        int completedCount = 0;
        int retainedCount = 0;
        
        for (Enrollment enrollment : transitionableEnrollments) {
            Long targetClassroomId;
            
            if (enrollment.getStatus() == EnrollmentStatus.COMPLETED) {
                if (nextClassroom == null) {
                    log.warn("Student {} is COMPLETED but no next classroom defined (final class?). Skipping.", 
                            enrollment.getStudentId());
                    continue;
                }
                targetClassroomId = nextClassroom.getId();
                completedCount++;
            } else { // RETAINED
                targetClassroomId = sameClassroom.getId();
                retainedCount++;
            }
            
            TransitionStaging staging = TransitionStaging.builder()
                    .studentId(enrollment.getStudentId())
                    .oldClassroomId(enrollment.getClassroomId())
                    .oldAcademicYear(enrollment.getAcademicYear())
                    .oldStudentCode(enrollment.getStudentCode())
                    .oldStatus(enrollment.getStatus())
                    .newClassroomId(targetClassroomId)
                    .newAcademicYear(request.getToAcademicYear())
                    .isFinalized(false)
                    .note("Auto-generated transition plan")
                    .build();
            
            stagingRecords.add(staging);
        }
        
        List<TransitionStaging> savedStaging = transitionStagingRepository.saveAll(stagingRecords);
        
        log.info("Created {} transition staging records ({} completed, {} retained)", 
                savedStaging.size(), completedCount, retainedCount);
        
        List<TransitionStagingResponseDto> transitionDtos = savedStaging.stream()
                .map(transitionMapper::toStagingResponseDto)
                .collect(Collectors.toList());
        
        TransitionPlanResponseDto.NextClassroomInfo nextClassroomInfo = null;
        if (nextClassroom != null) {
            nextClassroomInfo = TransitionPlanResponseDto.NextClassroomInfo.builder()
                    .id(nextClassroom.getId())
                    .name(nextClassroom.getName())
                    .academicYear(nextClassroom.getAcademicYear())
                    .build();
        }
        
        return TransitionPlanResponseDto.builder()
                .classroomId(request.getClassroomId())
                .classroomName(oldClassroom.getName())
                .fromAcademicYear(request.getFromAcademicYear())
                .toAcademicYear(request.getToAcademicYear())
                .totalStudents(transitionableEnrollments.size())
                .completedCount(completedCount)
                .retainedCount(retainedCount)
                .nextClassroom(nextClassroomInfo)
                .transitions(transitionDtos)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransitionStagingResponseDto> getPendingTransitions(Long classroomId, String academicYear) {
        log.info("Getting pending transitions for classroomId: {}, year: {}", classroomId, academicYear);
        
        List<TransitionStaging> pendingStaging = transitionStagingRepository
                .findByOldClassroomIdAndOldAcademicYearAndIsFinalized(classroomId, academicYear, false);
        
        log.info("Found {} pending transitions", pendingStaging.size());
        
        return pendingStaging.stream()
                .map(transitionMapper::toStagingResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public TransitionStagingResponseDto updateTransitionStaging(
            Long stagingId, 
            UpdateTransitionStagingRequest request) {
        log.info("Updating transition staging: {}", stagingId);
        
        TransitionStaging staging = transitionStagingRepository.findById(stagingId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Transition staging not found with id: " + stagingId));
        
        if (staging.getIsFinalized()) {
            throw new IllegalStateException(
                    "Cannot update finalized transition. Staging id: " + stagingId);
        }
        
        if (request.getNewClassroomId() != null) {
            Classroom newClassroom = classroomRepository.findById(request.getNewClassroomId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Classroom not found with id: " + request.getNewClassroomId()));
            
            staging.setNewClassroomId(request.getNewClassroomId());
            log.info("Changed target classroom to: {}", newClassroom.getName());
        }
        
        if (request.getNewAcademicYear() != null && !request.getNewAcademicYear().trim().isEmpty()) {
            staging.setNewAcademicYear(request.getNewAcademicYear());
            log.info("Changed target academic year to: {}", request.getNewAcademicYear());
        }
        
        if (request.getNote() != null) {
            staging.setNote(request.getNote());
        }
        
        TransitionStaging updatedStaging = transitionStagingRepository.save(staging);
        log.info("Updated transition staging: {}", stagingId);
        
        return transitionMapper.toStagingResponseDto(updatedStaging);
    }

    @Override
    @Transactional
    public void deleteTransitionStaging(Long stagingId) {
        log.info("Deleting transition staging: {}", stagingId);
        
        TransitionStaging staging = transitionStagingRepository.findById(stagingId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Transition staging not found with id: " + stagingId));
        
        if (staging.getIsFinalized()) {
            throw new IllegalStateException(
                    "Cannot delete finalized transition. Staging id: " + stagingId);
        }
        
        transitionStagingRepository.delete(staging);
        log.info("Deleted transition staging: {}", stagingId);
    }

    @Override
    @Transactional
    public int deletePendingTransitions(Long classroomId, String academicYear) {
        log.info("Deleting all pending transitions for classroomId: {}, year: {}", classroomId, academicYear);
        
        List<TransitionStaging> pendingStaging = transitionStagingRepository
                .findByOldClassroomIdAndOldAcademicYearAndIsFinalized(classroomId, academicYear, false);
        
        int count = pendingStaging.size();
        
        if (count > 0) {
            transitionStagingRepository.deleteAll(pendingStaging);
            log.info("Deleted {} pending transitions", count);
        } else {
            log.info("No pending transitions found to delete");
        }
        
        return count;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransitionStagingResponseDto> getStudentTransitionHistory(Long studentId) {
        log.info("Getting transition history for studentId: {}", studentId);
        
        List<TransitionStaging> history = transitionStagingRepository
                .findByStudentIdOrderByCreatedAtDesc(studentId);
        
        log.info("Found {} transition records for student {}", history.size(), studentId);
        
        return history.stream()
                .map(transitionMapper::toStagingResponseDto)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<TransitionStagingResponseDto> getIncomingTransitions(Long classroomId, String academicYear) {
        log.info("Getting incoming transitions for classroomId: {}, year: {}", classroomId, academicYear);
        
        List<TransitionStaging> incoming = transitionStagingRepository
                .findByNewClassroomIdAndNewAcademicYearAndIsFinalized(classroomId, academicYear, false);
        
        log.info("Found {} incoming students to classroom {}", incoming.size(), classroomId);
        
        return incoming.stream()
                .map(transitionMapper::toStagingResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public TransitionResultResponseDto finalizeTransition(FinalizeTransitionRequest request) {
        log.info("Finalizing transition for classroomId: {}, from: {} to: {}", 
                request.getClassroomId(), request.getFromAcademicYear(), request.getToAcademicYear());
        
        List<TransitionStaging> pendingStaging = transitionStagingRepository
                .findByOldClassroomIdAndOldAcademicYearAndIsFinalized(
                        request.getClassroomId(), 
                        request.getFromAcademicYear(), 
                        false);
        
        if (pendingStaging.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No pending transitions found for classroom " + request.getClassroomId() + 
                    " and year " + request.getFromAcademicYear());
        }
        
        log.info("Found {} pending transitions to finalize", pendingStaging.size());
        
        List<TransitionResultResponseDto.NewEnrollmentInfo> newEnrollments = new ArrayList<>();
        List<String> errors = new ArrayList<>();
        int successCount = 0;
        int failedCount = 0;
        
        for (TransitionStaging staging : pendingStaging) {
            try {
                // create new enrollment (without student code first)
                Enrollment newEnrollment = Enrollment.builder()
                        .studentId(staging.getStudentId())
                        .classroomId(staging.getNewClassroomId())
                        .academicYear(staging.getNewAcademicYear())
                        .status(EnrollmentStatus.ACTIVE)
                        .enrollmentDate(LocalDate.now())
                        .note("Auto-created by transition from " + staging.getOldAcademicYear())
                        .build();
                
                Enrollment savedEnrollment = enrollmentRepository.save(newEnrollment);
                
                staging.setNewEnrollmentId(savedEnrollment.getId());
                
                successCount++;
                
                log.debug("Created enrollment {} for student {}", 
                        savedEnrollment.getId(), staging.getStudentId());
                
            } catch (Exception e) {
                failedCount++;
                String errorMsg = "Failed to create enrollment for student " + 
                        staging.getStudentId() + ": " + e.getMessage();
                errors.add(errorMsg);
                log.error(errorMsg, e);
            }
        }
        
        if (request.getAutoGenerateStudentCodes() && successCount > 0) {
            log.info("Generating student codes for new enrollments");
            
            Map<Long, List<TransitionStaging>> byClassroom = pendingStaging.stream()
                    .filter(s -> s.getNewEnrollmentId() != null) // only successful ones
                    .collect(Collectors.groupingBy(TransitionStaging::getNewClassroomId));
            
            for (Map.Entry<Long, List<TransitionStaging>> entry : byClassroom.entrySet()) {
                Long classroomId = entry.getKey();
                String academicYear = request.getToAcademicYear();
                
                try {
                    BatchStudentCodeService.BatchGenerateResult codeResult = 
                            batchStudentCodeService.generateCodesForClassroom(
                                    classroomId, 
                                    academicYear, 
                                    false);
                    
                    log.info("Generated {} student codes for classroom {}", 
                            codeResult.getGeneratedCount(), classroomId);
                    
                    for (TransitionStaging staging : entry.getValue()) {
                        enrollmentRepository.findById(staging.getNewEnrollmentId())
                                .ifPresent(enrollment -> {
                                    staging.setNewStudentCode(enrollment.getStudentCode());
                                });
                    }
                    
                } catch (Exception e) {
                    String errorMsg = "Failed to generate codes for classroom " + 
                            classroomId + ": " + e.getMessage();
                    errors.add(errorMsg);
                    log.error(errorMsg, e);
                }
            }
        }
        
        for (TransitionStaging staging : pendingStaging) {
            if (staging.getNewEnrollmentId() != null) { // only if enrollment was created
                staging.setIsFinalized(true);
                staging.setFinalizedAt(LocalDateTime.now());
            }
        }
        
        transitionStagingRepository.saveAll(pendingStaging);
        
        for (TransitionStaging staging : pendingStaging) {
            if (staging.getNewEnrollmentId() != null) {
                newEnrollments.add(transitionMapper.toNewEnrollmentInfo(staging));
            }
        }
        
        String message = String.format(
                "Transition finalized: %d successful, %d failed", 
                successCount, failedCount);
        
        log.info(message);
        
        return TransitionResultResponseDto.builder()
                .message(message)
                .totalProcessed(pendingStaging.size())
                .successCount(successCount)
                .failedCount(failedCount)
                .newEnrollments(newEnrollments)
                .errors(errors.isEmpty() ? null : errors)
                .build();
    }
    
    // 
    @Override
    @Transactional
    public FinalizeAllTransitionsResponseDto finalizeAllTransitions(FinalizeAllTransitionsRequest request) {
        log.info("Finalizing all transitions from {} to {}, classrooms: {}", 
                request.getFromAcademicYear(), request.getToAcademicYear(), request.getClassroomIds());
        
        List<TransitionStaging> allPendingStaging;
        
        if (request.getClassroomIds() != null && !request.getClassroomIds().isEmpty()) {
            // finalize only specific classrooms
            allPendingStaging = transitionStagingRepository
                    .findByOldClassroomIdInAndOldAcademicYearAndIsFinalized(
                            request.getClassroomIds(), 
                            request.getFromAcademicYear(), 
                            false);
        } else {
            // finalize ALL pending transitions for the academic year
            allPendingStaging = transitionStagingRepository
                    .findByOldAcademicYearAndIsFinalized(request.getFromAcademicYear(), false);
        }
        
        if (allPendingStaging.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No pending transitions found for academic year " + request.getFromAcademicYear());
        }
        
        // group by classroom
        Map<Long, List<TransitionStaging>> stagingByClassroom = allPendingStaging.stream()
                .collect(Collectors.groupingBy(TransitionStaging::getOldClassroomId));
        
        log.info("Found {} classrooms with {} total pending transitions", 
                stagingByClassroom.size(), allPendingStaging.size());
        
        // process each classroom
        List<FinalizeAllTransitionsResponseDto.ClassroomResult> classroomResults = new ArrayList<>();
        int totalClassrooms = stagingByClassroom.size();
        int successfulClassrooms = 0;
        int failedClassrooms = 0;
        int totalStudents = 0;
        int successfulStudents = 0;
        int failedStudents = 0;
        List<String> globalErrors = new ArrayList<>();
        
        for (Map.Entry<Long, List<TransitionStaging>> entry : stagingByClassroom.entrySet()) {
            Long classroomId = entry.getKey();
            
            try {
                // get classroom name
                String classroomName = classroomRepository.findById(classroomId)
                        .map(Classroom::getName)
                        .orElse("Unknown");
                
                log.info("Processing classroom {} ({})", classroomId, classroomName);
                
                // create finalize request for this classroom
                FinalizeTransitionRequest classroomRequest = FinalizeTransitionRequest.builder()
                        .classroomId(classroomId)
                        .fromAcademicYear(request.getFromAcademicYear())
                        .toAcademicYear(request.getToAcademicYear())
                        .autoGenerateStudentCodes(request.getAutoGenerateStudentCodes())
                        .build();
                
                // finalize this classroom
                TransitionResultResponseDto result = finalizeTransition(classroomRequest);
                
                // build classroom result
                FinalizeAllTransitionsResponseDto.ClassroomResult classroomResult = 
                        FinalizeAllTransitionsResponseDto.ClassroomResult.builder()
                                .classroomId(classroomId)
                                .classroomName(classroomName)
                                .success(result.getFailedCount() == 0)
                                .studentsProcessed(result.getTotalProcessed())
                                .studentsSuccess(result.getSuccessCount())
                                .studentsFailed(result.getFailedCount())
                                .newEnrollments(result.getNewEnrollments())
                                .errorMessage(result.getErrors() != null && !result.getErrors().isEmpty() 
                                        ? String.join("; ", result.getErrors()) 
                                        : null)
                                .build();
                
                classroomResults.add(classroomResult);
                
                // update totals
                totalStudents += result.getTotalProcessed();
                successfulStudents += result.getSuccessCount();
                failedStudents += result.getFailedCount();
                
                if (result.getFailedCount() == 0) {
                    successfulClassrooms++;
                } else {
                    failedClassrooms++;
                }
                
                log.info("Classroom {} completed: {} students ({} success, {} failed)", 
                        classroomId, result.getTotalProcessed(), result.getSuccessCount(), result.getFailedCount());
                
            } catch (Exception e) {
                failedClassrooms++;
                String errorMsg = "Failed to finalize classroom " + classroomId + ": " + e.getMessage();
                globalErrors.add(errorMsg);
                log.error(errorMsg, e);
                
                // add failed classroom result
                String classroomName = classroomRepository.findById(classroomId)
                        .map(Classroom::getName)
                        .orElse("Unknown");
                
                FinalizeAllTransitionsResponseDto.ClassroomResult classroomResult = 
                        FinalizeAllTransitionsResponseDto.ClassroomResult.builder()
                                .classroomId(classroomId)
                                .classroomName(classroomName)
                                .success(false)
                                .studentsProcessed(0)
                                .studentsSuccess(0)
                                .studentsFailed(entry.getValue().size())
                                .errorMessage(e.getMessage())
                                .build();
                
                classroomResults.add(classroomResult);
            }
        }
        
        String message = String.format(
                "Batch finalization completed: %d/%d classrooms successful, %d/%d students transitioned", 
                successfulClassrooms, totalClassrooms, successfulStudents, totalStudents);
        
        log.info(message);
        
        return FinalizeAllTransitionsResponseDto.builder()
                .message(message)
                .totalClassrooms(totalClassrooms)
                .successfulClassrooms(successfulClassrooms)
                .failedClassrooms(failedClassrooms)
                .totalStudents(totalStudents)
                .successfulStudents(successfulStudents)
                .failedStudents(failedStudents)
                .classroomResults(classroomResults)
                .globalErrors(globalErrors.isEmpty() ? null : globalErrors)
                .build();
    }
}
