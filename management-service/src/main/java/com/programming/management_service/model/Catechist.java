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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long userId;

    @Enumerated(EnumType.STRING)
    private CatechistType type; // DU_TRUONG, HUYNH_TRUONG

    private String responsibility;

    @ManyToOne
    @JoinColumn(name = "classroom_id")
    private Classroom classroom;

    @Transient
    private UserDto user;
}