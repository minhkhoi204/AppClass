package com.programming.management_service.domain.model;

public enum SessionType {
    BEFORE_CLASS("Trước giờ học"),
    AFTER_CLASS("Sau giờ học");

    private final String displayName;

    SessionType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
