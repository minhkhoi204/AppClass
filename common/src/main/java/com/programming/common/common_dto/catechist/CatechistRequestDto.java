package com.programming.common.common_dto.catechist;

import com.programming.common.common_auth.Role;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CatechistRequestDto {
    private Long userId;

    private String fullName;
    private String christianName;
    private LocalDate dateOfBirth;

    private Role role;
    private LocalDate promiseDate;
    private Boolean isExecutiveBoard;
    private String note;
}
