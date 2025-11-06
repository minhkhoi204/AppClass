package com.programming.management_service.management_caller;

import com.programming.common.response.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(
        name = "user-service",
        url = "${user.service.url}",
        contextId = "catechistClient"
)
public interface CatechistClient {

    @GetMapping("/api/catechists/{id}")
    ApiResponse getCatechistById(@PathVariable("id") Long id);
}
