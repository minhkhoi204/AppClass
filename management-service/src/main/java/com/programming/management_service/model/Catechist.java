package com.programming.management_service.model;

import com.programming.management_service.constant.CatechistType;
import com.programming.management_service.dto.UserDto;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "catechists")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Catechist {

    @Id
    private Long userId;

    @Enumerated(EnumType.STRING)
    private CatechistType type;

    private String responsibility;
    private String classAssigned;

    @Transient
    private UserDto user;
}