package com.programming.user_service.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name; // ADMIN, BAN_DIEU_HANH, GIAO_LY_VIEN, DU_TRUONG, THIEU_NHI

    private String description; // display on UI
}
