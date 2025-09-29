package com.programming.management_service.dto;

import lombok.Data;

@Data
public class CatechistDto {
    private Long userId;
    private String saintName;
    private String fullName;
    private String phone;
    private String email;
    private String type; // DU_TRUONG or HUYNH_TRUONG
    private String responsibility;
}
