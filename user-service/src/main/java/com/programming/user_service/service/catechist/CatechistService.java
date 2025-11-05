package com.programming.user_service.service.catechist;

import com.programming.common.common_dto.catechist.CatechistRequestDto;
import com.programming.common.common_dto.catechist.CatechistResponseDto;
import java.util.List;

public interface CatechistService {
    CatechistResponseDto createCatechist(CatechistRequestDto requestDto);
    CatechistResponseDto createCatechistWithUserId(CatechistRequestDto requestDto, Long userId);
    CatechistResponseDto updateCatechist(Long id, CatechistRequestDto requestDto);
    void deleteCatechist(Long id);
    CatechistResponseDto getCatechistById(Long id);
    List<CatechistResponseDto> getAllCatechists();
}
