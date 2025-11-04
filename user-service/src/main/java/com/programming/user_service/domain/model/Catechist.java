package com.programming.user_service.domain.model;

import com.programming.common.common_auth.Role;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "catechists")
@Setter
@Getter
public class Catechist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    private Role role; 
    
    @Column(name = "promise_date")
    private LocalDate promiseDate; // Ngày tuyên hứa (null = chưa tuyên hứa/Dự Trưởng)
    
    private Boolean isExecutiveBoard; // check if catechist is in executive board

    private String note;
    
    // Helper methods
    public boolean hasPromised() {
        return promiseDate != null;
    }
    
    public boolean isDuTruong() {
        return role == Role.DU_TRUONG;
    }
    
    public boolean isHuynhTruong() {
        return role == Role.HUYNH_TRUONG;
    }
}
