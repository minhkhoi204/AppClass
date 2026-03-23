package com.programming.management_service.management_caller;

import com.programming.common.common_dto.student.StudentResponseDto;
import com.programming.management_service.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "user-service",
        url = "${user.service.url}",
        contextId = "userClient",
        configuration = FeignConfig.class
)
public interface UserClient {
    @GetMapping("/api/students/{id}")
    StudentResponseDto getUserById(@PathVariable("id") Long userId);
}
