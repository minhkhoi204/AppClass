package com.programming.user_service.controller;

import com.programming.common.common_dto.catechist.CatechistRequestDto;
import com.programming.common.common_dto.catechist.CatechistResponseDto;
import com.programming.common.response.ApiResponse;
import com.programming.user_service.service.catechist.CatechistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/catechists")
public class CatechistController {
    @Autowired
    private CatechistService catechistService;

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('DOAN_TRUONG', 'PHO_NOI', 'PHO_NGOAI')")
    public ResponseEntity<ApiResponse> createCatechist(
            @RequestBody CatechistRequestDto requestDto) {
        CatechistResponseDto response = catechistService.createCatechist(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse("Catechist created successfully", response));
    }

    @PostMapping("/create-with-user")
    @PreAuthorize("hasAnyRole('DOAN_TRUONG', 'PHO_NOI', 'PHO_NGOAI')")
    public ResponseEntity<ApiResponse> createCatechistWithUserId(
            @RequestBody CatechistRequestDto requestDto,
            @RequestParam Long userId) {
        CatechistResponseDto response = catechistService.createCatechistWithUserId(requestDto, userId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse("Catechist created successfully", response));
    }

    @PutMapping("/{id}/update")
    @PreAuthorize("hasAnyRole('DOAN_TRUONG', 'PHO_NOI', 'PHO_NGOAI')")
    public ResponseEntity<ApiResponse> updateCatechist(
            @PathVariable Long id, 
            @RequestBody CatechistRequestDto requestDto) {
        CatechistResponseDto response = catechistService.updateCatechist(id, requestDto);
        return ResponseEntity.ok(new ApiResponse("Catechist updated successfully", response));
    }

    @DeleteMapping("/{id}/delete")
    @PreAuthorize("hasAnyRole('DOAN_TRUONG', 'PHO_NOI', 'PHO_NGOAI')")
    public ResponseEntity<Void> deleteCatechist(@PathVariable Long id) {
        catechistService.deleteCatechist(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse> getCatechistById(@PathVariable Long id) {
        CatechistResponseDto response = catechistService.getCatechistById(id);
        return ResponseEntity.ok(new ApiResponse("Catechist retrieved successfully", response));
    }

    @GetMapping("/all")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse> getAllCatechists() {
        List<CatechistResponseDto> response = catechistService.getAllCatechists();
        return ResponseEntity.ok(new ApiResponse("All catechists retrieved successfully", response));
    }
}
