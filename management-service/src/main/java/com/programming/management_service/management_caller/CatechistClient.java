package com.programming.management_service.management_caller;

import com.programming.common.common_dto.catechist.CatechistRequestDto;
import com.programming.common.common_dto.catechist.CatechistResponseDto;
import com.programming.common.common_dto.student.StudentRequestDto;
import com.programming.common.common_dto.student.StudentResponseDto;
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

    @PutMapping("/api/catechists/{id}/update")
    CatechistResponseDto updateCatechist(@PathVariable("id") Long catechistId,
                                       @RequestBody CatechistRequestDto updatedCatechistDto);
}
