package com.example.buffet.model;

import java.time.Instant;

public class QueueItem {
    private final String id;
    private final String name;
    private final String phone;
    private final int count;
    private final Instant joinedAt;

    public QueueItem(String id, String name, String phone, int count) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.count = count;
        this.joinedAt = Instant.now();
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public int getCount() { return count; }
    public Instant getJoinedAt() { return joinedAt; }

    public long getWaitedMinutes() {
        return java.time.Duration.between(joinedAt, Instant.now()).toMinutes();
    }

    public long getJoinedAtMillis() {
        return joinedAt.toEpochMilli();
    }
}
