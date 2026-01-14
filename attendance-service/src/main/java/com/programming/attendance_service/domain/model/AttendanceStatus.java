package com.programming.attendance_service.domain.model;

public enum AttendanceStatus {
    PRESENT("Có mặt"),
    ABSENT("Vắng mặt"),
    LATE("Đi muộn"),
    EXCUSED("Vắng có phép");

    private final String displayName;

    AttendanceStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
