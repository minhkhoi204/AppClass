package com.programming.management_service.controller;

import com.programming.management_service.domain.dto.request.ClassroomRequestDto;
import com.programming.management_service.domain.dto.response.ClassroomResponseDto;
import com.programming.management_service.mapper.ClassroomMapper;

import com.programming.common.response.ApiResponse;
import com.programming.management_service.domain.model.Classroom;

import com.programming.management_service.service.classroom.ClassroomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/classrooms")

public class ClassroomController {

    private final ClassroomService classroomService;
    private final ClassroomMapper classroomMapper;

    // Create class
    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createClassroom(@RequestBody ClassroomRequestDto classroomRequestDto) {
        ClassroomResponseDto responseDto = classroomService.createClassroom(classroomRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse("Classroom created successfully", responseDto));
    }

    @PostMapping("/{classroomId}/students/{studentId}/add")
    public ResponseEntity<ApiResponse> addStudentToClassroom(@PathVariable Long classroomId,
                                                             @PathVariable Long studentId) {
        classroomService.addStudentToClassroom(classroomId, studentId);
        return ResponseEntity.ok(new ApiResponse("Student added to classroom successfully", null));
    }

    @DeleteMapping("/{classroomId}/students/{studentId}/remove")
    public ResponseEntity<ApiResponse> removeStudentFromClassroom(@PathVariable Long classroomId,
                                                                  @PathVariable Long studentId) {
        classroomService.removeStudentFromClassroom(classroomId, studentId);
        return ResponseEntity.ok(new ApiResponse("Student removed from classroom successfully", null));
    }

    @PostMapping("/{classroomId}/catechists/{catechistId}/add")
    public ResponseEntity<ApiResponse> addCatechistToClassroom(@PathVariable Long classroomId,
                                                               @PathVariable Long catechistId) {
        classroomService.addCatechistToClassroom(classroomId, catechistId);
        return ResponseEntity.ok(new ApiResponse("Catechist added to classroom successfully", null));
    }

    @DeleteMapping("/{classroomId}/catechists/{catechistId}/remove")
    public ResponseEntity<ApiResponse> removeCatechistFromClassroom(@PathVariable Long classroomId,
                                                                    @PathVariable Long catechistId) {
        classroomService.removeCatechistFromClassroom(classroomId, catechistId);
        return ResponseEntity.ok(new ApiResponse("Catechist removed from classroom successfully", null));
    }

    @GetMapping("/{classroomId}")
    public ResponseEntity<ApiResponse> getClassroomById(@PathVariable Long classroomId) {
        ClassroomResponseDto classroom = classroomService.getClassroomById(classroomId);
        return ResponseEntity.ok(new ApiResponse("Success", classroom));
    }

    @PutMapping("/{classroomId}/update")
    public ResponseEntity<ApiResponse> updateClassroom(@PathVariable Long classroomId,
                                                       @RequestBody ClassroomRequestDto classroomRequestDto) {
        ClassroomResponseDto updated = classroomService.updateClassroom(classroomId, classroomRequestDto);
        return ResponseEntity.ok(new ApiResponse("Classroom updated successfully", updated));
    }

}
