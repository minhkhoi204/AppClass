package com.programming.common.common_dto.catechist;

import com.programming.common.common_auth.Role;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CatechistResponseDto {
    private Long id;
    private Long userId;
    
    private String fullName;
    private String christianName;
    private LocalDate dateOfBirth;
    
    private Role role;
    private LocalDate assistantCatechistPromiseDate; // Promise date for DU_TRUONG (Assistant Catechist)
    private LocalDate catechistPromiseDate; // Promise date for HUYNH_TRUONG (Catechist)
    private Boolean isExecutiveBoard;
    private String note;
}
