package com.programming.user_service.model;

import com.programming.user_service.constant.CatechistType;
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

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    private String classAssigned;

    @Enumerated(EnumType.STRING)
    private CatechistType type; // HUYNH_TRUONG, DU_TRUONG

    private String responsibility; // nhiệm vụ cụ thể
}