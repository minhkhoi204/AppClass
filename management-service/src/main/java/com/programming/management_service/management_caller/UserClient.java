package com.programming.management_service.management_caller;

import com.programming.management_service.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(
        name = "user-service",
        url = "${user.service.url}",
        contextId = "userClient"
)
public interface UserClient {
    @GetMapping("/api/users/{id}")
    UserDto getUserById(@PathVariable("id") Long id);

    @GetMapping("/api/users/{id}/exists")
    Boolean userExists(@PathVariable("id") Long id);
}
