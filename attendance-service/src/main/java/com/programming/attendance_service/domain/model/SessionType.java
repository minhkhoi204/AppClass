package com.programming.attendance_service.domain.model;

public enum SessionType {
    BEFORE_CLASS("Trước giờ học"),
    AFTER_CLASS("Sau giờ học"),
    MASS_ATTENDANCE("Tham dự thánh lễ");

    private final String displayName;

    SessionType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
