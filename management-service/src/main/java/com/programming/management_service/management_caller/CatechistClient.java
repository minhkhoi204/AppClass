package com.programming.management_service.management_caller;

import com.programming.common.common_dto.catechist.CatechistRequestDto;
import com.programming.common.common_dto.catechist.CatechistResponseDto;
import com.programming.common.common_dto.student.StudentRequestDto;
import com.programming.common.common_dto.student.StudentResponseDto;
import com.programming.common.response.ApiResponse;
import com.programming.management_service.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(
        name = "user-service",
        url = "${user.service.url}",
        contextId = "catechistClient",
        configuration = FeignConfig.class
)
public interface CatechistClient {

    @GetMapping("/api/catechists/{id}")
    ApiResponse getCatechistById(@PathVariable("id") Long id);

    @GetMapping("/api/catechists/batch")
    List<CatechistResponseDto> getCatechistsByIds(@RequestParam("ids") List<Long> ids);

    @PutMapping("/api/catechists/{id}/update")
    CatechistResponseDto updateCatechist(@PathVariable("id") Long catechistId,
                                       @RequestBody CatechistRequestDto updatedCatechistDto);
}
