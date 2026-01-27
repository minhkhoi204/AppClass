package com.programming.management_service.controller;

import com.programming.common.common_dto.catechist.CatechistResponseDto;
import com.programming.common.common_dto.student.StudentResponseDto;
import com.programming.management_service.domain.dto.request.ClassroomRequestDto;
import com.programming.management_service.domain.dto.response.ClassroomResponseDto;
import com.programming.management_service.mapper.ClassroomMapper;

import com.programming.common.response.ApiResponse;
import com.programming.management_service.domain.model.Classroom;

import com.programming.management_service.service.classroom.ClassroomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/classrooms")

public class ClassroomController {

    private final ClassroomService classroomService;
    private final ClassroomMapper classroomMapper;

    // Create class - Only Executive Board
    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('DOAN_TRUONG', 'PHO_NOI', 'PHO_NGOAI')")
    public ResponseEntity<ApiResponse> createClassroom(@RequestBody ClassroomRequestDto classroomRequestDto) {
        ClassroomResponseDto responseDto = classroomService.createClassroom(classroomRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse("Classroom created successfully", responseDto));
    }

    /*
    @PostMapping("/{classroomId}/students/{studentId}/add")
    @PreAuthorize("hasAnyRole('DOAN_TRUONG', 'PHO_NOI', 'PHO_NGOAI', 'THU_KY')")
    public ResponseEntity<ApiResponse> addStudentToClassroom(@PathVariable Long classroomId,
                                                             @PathVariable Long studentId) {
        classroomService.addStudentToClassroom(classroomId, studentId);
        return ResponseEntity.ok(new ApiResponse("Student added to classroom successfully", null));
    }

    @DeleteMapping("/{classroomId}/students/{studentId}/remove")
    @PreAuthorize("hasAnyRole('DOAN_TRUONG', 'PHO_NOI', 'PHO_NGOAI', 'THU_KY')")
    public ResponseEntity<ApiResponse> removeStudentFromClassroom(@PathVariable Long classroomId,
                                                                  @PathVariable Long studentId) {
        classroomService.removeStudentFromClassroom(classroomId, studentId);
        return ResponseEntity.ok(new ApiResponse("Student removed from classroom successfully", null));
    }
    */

    /*
    @PostMapping("/{classroomId}/catechists/{catechistId}/add")
    @PreAuthorize("hasAnyRole('DOAN_TRUONG', 'PHO_NOI', 'PHO_NGOAI')")
    public ResponseEntity<ApiResponse> addCatechistToClassroom(@PathVariable Long classroomId,
                                                               @PathVariable Long catechistId) {
        classroomService.addCatechistToClassroom(classroomId, catechistId);
        return ResponseEntity.ok(new ApiResponse("Catechist added to classroom successfully", null));
    }

    @DeleteMapping("/{classroomId}/catechists/{catechistId}/remove")
    @PreAuthorize("hasAnyRole('DOAN_TRUONG', 'PHO_NOI', 'PHO_NGOAI')")
    public ResponseEntity<ApiResponse> removeCatechistFromClassroom(@PathVariable Long classroomId,
                                                                    @PathVariable Long catechistId) {
        classroomService.removeCatechistFromClassroom(classroomId, catechistId);
        return ResponseEntity.ok(new ApiResponse("Catechist removed from classroom successfully", null));
    }
    */

    @GetMapping("/{classroomId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse> getClassroomById(@PathVariable Long classroomId) {
        ClassroomResponseDto classroom = classroomService.getClassroomById(classroomId);
        return ResponseEntity.ok(new ApiResponse("Success", classroom));
    }

    @PutMapping("/{classroomId}/update")
    @PreAuthorize("hasAnyRole('DOAN_TRUONG', 'PHO_NOI', 'PHO_NGOAI')")
    public ResponseEntity<ApiResponse> updateClassroom(@PathVariable Long classroomId,
                                                       @RequestBody ClassroomRequestDto classroomRequestDto) {
        ClassroomResponseDto updated = classroomService.updateClassroom(classroomId, classroomRequestDto);
        return ResponseEntity.ok(new ApiResponse("Classroom updated successfully", updated));
    }

    // get all students in a classroom
    @GetMapping("/{classroomId}/students")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<List<StudentResponseDto>>> getStudentsInClassroom(
            @PathVariable Long classroomId,
            @RequestParam String academicYear) {
        List<StudentResponseDto> students = classroomService.getStudentsInClassroom(classroomId, academicYear);
        return ResponseEntity.ok(ApiResponse.success(students, 
                "Found " + students.size() + " student(s) in classroom"));
    }

    // get all catechists in a classroom
    @GetMapping("/{classroomId}/catechists")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<List<CatechistResponseDto>>> getCatechistsInClassroom(
            @PathVariable Long classroomId,
            @RequestParam String academicYear) {
        List<CatechistResponseDto> catechists = classroomService.getCatechistsInClassroom(classroomId, academicYear);
        return ResponseEntity.ok(ApiResponse.success(catechists, 
                "Found " + catechists.size() + " catechist(s) in classroom"));
    }

}
