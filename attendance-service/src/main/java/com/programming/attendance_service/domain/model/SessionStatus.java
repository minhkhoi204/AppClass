package com.programming.attendance_service.domain.model;

public enum SessionStatus {
    OPEN("Đang mở"),
    CLOSED("Đã đóng"),
    CANCELLED("Đã hủy");

    private final String displayName;

    SessionStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
