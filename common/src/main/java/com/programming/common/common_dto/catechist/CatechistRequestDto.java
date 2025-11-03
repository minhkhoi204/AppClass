package com.programming.common_dto.catechist;

import com.programming.user_service.domain.enums.Role;
import lombok.Data;

@Data
public class CatechistRequestDto {
    private Long userId;
    private Role role;
    private Boolean isExecutiveBoard;
    private String note;
}
