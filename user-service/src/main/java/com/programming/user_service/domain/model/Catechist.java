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
    
    @Column(name = "assistant_catechist_promise_date")
    private LocalDate assistantCatechistPromiseDate; // Promise date for DU_TRUONG (Assistant Catechist)
    
    @Column(name = "catechist_promise_date")
    private LocalDate catechistPromiseDate; // Promise date for HUYNH_TRUONG (Catechist)
    
    private Boolean isExecutiveBoard; // check if catechist is in executive board

    private String note;
    
    // Helper methods
    public boolean hasAssistantCatechistPromised() {
        return assistantCatechistPromiseDate != null;
    }
    
    public boolean hasCatechistPromised() {
        return catechistPromiseDate != null;
    }
    
    public boolean isDuTruong() {
        return role == Role.DU_TRUONG;
    }
    
    public boolean isHuynhTruong() {
        return role == Role.HUYNH_TRUONG;
    }
}
