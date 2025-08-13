package com.example.buffet.model;

public enum TableStatus {
    AVAILABLE("ว่าง"),
    OCCUPIED("กำลังทาน"),
    CLEANING("ทำความสะอาด");

    private final String displayName;

    TableStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
