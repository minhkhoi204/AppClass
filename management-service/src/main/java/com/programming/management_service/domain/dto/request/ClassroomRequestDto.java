package com.programming.management_service.domain.dto.request;

import lombok.Data;

import java.util.Set;

@Data
public class ClassroomRequestDto {
    private String name;
    private Set<Long> studentIds;
}
