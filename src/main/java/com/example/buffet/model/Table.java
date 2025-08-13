package com.example.buffet.model;

import java.time.Instant;

public class Table {
    private final int id;
    private final int capacity;
    private TableStatus status;
    private String customerName;
    private int customerCount;
    private Instant startTime;

    public Table(int id, int capacity) {
        this.id = id;
        this.capacity = capacity;
        this.status = TableStatus.AVAILABLE;
    }

    // Getters
    public int getId() { return id; }
    public int getCapacity() { return capacity; }
    public TableStatus getStatus() { return status; }
    public String getCustomerName() { return customerName; }
    public int getCustomerCount() { return customerCount; }
    public Instant getStartTime() { return startTime; }

    // Setters
    public void setStatus(TableStatus status) { this.status = status; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public void setCustomerCount(int customerCount) { this.customerCount = customerCount; }
    public void setStartTime(Instant startTime) { this.startTime = startTime; }

    public void occupy(String customerName, int customerCount) {
        this.customerName = customerName;
        this.customerCount = customerCount;
        this.status = TableStatus.OCCUPIED;
        this.startTime = Instant.now();
    }

    public void startCleaning() {
        this.customerName = null;
        this.customerCount = 0;
        this.status = TableStatus.CLEANING;
        this.startTime = Instant.now();
    }

    public void makeAvailable() {
        this.status = TableStatus.AVAILABLE;
        this.startTime = null;
    }

    public long getElapsedMinutes() {
        if (startTime == null) {
            return 0;
        }
        return java.time.Duration.between(startTime, Instant.now()).toMinutes();
    }

    public long getStartTimeMillis() {
        if (startTime == null) {
            return 0;
        }
        return startTime.toEpochMilli();
    }
}
