package com.programming.management_service.dto;

import com.programming.management_service.constant.CatechistType;
import lombok.Data;

@Data
public class CatechistDto {
    private Long id;
    private String responsibility;
    private CatechistType type;
    private Long userId;
    private String fullName; // from user
    private String saintName; // from user
    private Long classAssignedId;
    private String classAssignedName; // optional
}