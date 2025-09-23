package com.programming.user_service.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "executive_boards")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Executive {

    @Id
    private Long userId; // Dùng chung ID với User

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    private String position; // chức vụ (trưởng đoàn, phó đoàn)
}