package com.programming.attendance_service.client;

import com.programming.common.response.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
    name = "management-service",
    url = "${management.service.url}"
)
public interface ManagementServiceClient {
    
    @GetMapping("/api/classrooms/{classroomId}")
    ApiResponse getClassroomById(@PathVariable("classroomId") Long classroomId);
    
    @GetMapping("/api/classrooms")
    ApiResponse getAllClassrooms();
}
