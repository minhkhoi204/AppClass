package com.programming.management_service.model;

import com.programming.management_service.constant.ExecutivePosition;
import com.programming.management_service.dto.UserDto;
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
    private Long userId;

    @Enumerated(EnumType.STRING)
    private ExecutivePosition position;

    @Transient
    private UserDto user;
}