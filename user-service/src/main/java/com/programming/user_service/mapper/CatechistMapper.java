package com.programming.user_service.mapper;

import com.programming.common.common_dto.catechist.CatechistRequestDto;
import com.programming.common.common_dto.catechist.CatechistResponseDto;
import com.programming.user_service.domain.model.Catechist;

public class CatechistMapper {
    public static Catechist toCatechistEntity(CatechistRequestDto dto) {
        Catechist catechist = new Catechist();

        catechist.setRole(dto.getRole());
        catechist.setPromiseDate(dto.getPromiseDate());
        catechist.setIsExecutiveBoard(dto.getIsExecutiveBoard());
        catechist.setNote(dto.getNote());
        return catechist;
    }

    public static CatechistResponseDto toCatechistDto(Catechist catechist) {
        if (catechist == null) return null;

        return CatechistResponseDto.builder()
                .id(catechist.getId())
                .userId(catechist.getUser().getId())

                .fullName(catechist.getUser().getFullName())
                .christianName(catechist.getUser().getChristianName())
                .dateOfBirth(catechist.getUser().getDateOfBirth())

                .role(catechist.getRole())
                .promiseDate(catechist.getPromiseDate())
                .isExecutiveBoard(catechist.getIsExecutiveBoard())
                .note(catechist.getNote())
                .build();
    }
}

