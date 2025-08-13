package com.example.buffet.service;

import com.example.buffet.model.QueueItem;
import com.example.buffet.model.Table;
import com.example.buffet.model.TableStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class BuffetManager {

    private static final BuffetManager INSTANCE = new BuffetManager();

    private final List<Table> tables = new CopyOnWriteArrayList<>();
    private final List<QueueItem> queue = new CopyOnWriteArrayList<>();
    private final AtomicInteger queueNumber = new AtomicInteger(1);
    private int totalServed = 0;

    private BuffetManager() {
        // Initialize tables
        tables.add(new Table(1, 2));
        tables.add(new Table(2, 4));
        tables.add(new Table(3, 6));
        tables.add(new Table(4, 2));
        tables.add(new Table(5, 8));
        tables.add(new Table(6, 4));
        tables.add(new Table(7, 2));
        tables.add(new Table(8, 6));
        tables.add(new Table(9, 8));
        tables.add(new Table(10, 4));
        tables.add(new Table(11, 2));
        tables.add(new Table(12, 8));
    }

    public static BuffetManager getInstance() {
        return INSTANCE;
    }

    public List<Table> getTables() {
        return Collections.unmodifiableList(tables);
    }

    public List<QueueItem> getQueue() {
        return Collections.unmodifiableList(queue);
    }

    public void addQueue(String name, String phone, int count) {
        String id = "A" + String.format("%03d", queueNumber.getAndIncrement());
        queue.add(new QueueItem(id, name, phone, count));
    }

    public void cancelQueue(String queueId) {
        queue.removeIf(item -> item.getId().equals(queueId));
    }

    public boolean assignTable(String queueId) {
        QueueItem item = findQueueItem(queueId);
        if (item == null) return false;

        List<Table> availableTables = new ArrayList<>();
        for (Table t : tables) {
            if (t.getStatus() == TableStatus.AVAILABLE) {
                availableTables.add(t);
            }
        }

        // Simple logic: find the smallest table that fits
        Table bestFit = availableTables.stream()
                .filter(t -> t.getCapacity() >= item.getCount())
                .min((t1, t2) -> Integer.compare(t1.getCapacity(), t2.getCapacity()))
                .orElse(null);

        if (bestFit != null) {
            bestFit.occupy(item.getName(), item.getCount());
            queue.remove(item);
            totalServed++;
            return true;
        }
        return false;
    }

    public void freeTable(int tableId) {
        Table table = findTable(tableId);
        if (table != null && table.getStatus() == TableStatus.OCCUPIED) {
            table.startCleaning();
            // In a real app, you'd have a timer to move it to AVAILABLE
            // For simplicity, we'll make it available immediately after a short delay in a separate thread
            new Thread(() -> {
                try {
                    Thread.sleep(5000); // 5 seconds for cleaning
                    table.makeAvailable();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }).start();
        }
    }
    
    public void finishCleaning(int tableId) {
        Table table = findTable(tableId);
        if (table != null && table.getStatus() == TableStatus.CLEANING) {
            table.makeAvailable();
        }
    }

    public Table findTable(int tableId) {
        return tables.stream().filter(t -> t.getId() == tableId).findFirst().orElse(null);
    }

    public QueueItem findQueueItem(String queueId) {
        return queue.stream().filter(q -> q.getId().equals(queueId)).findFirst().orElse(null);
    }

    // --- Stats ---
    public int getWaitingCount() { return queue.size(); }
    public long getEatingCount() { return tables.stream().filter(t -> t.getStatus() == TableStatus.OCCUPIED).count(); }
    public long getAvailableTables() { return tables.stream().filter(t -> t.getStatus() == TableStatus.AVAILABLE).count(); }
    public int getTotalServed() { return totalServed; }
    public int getTotalPeople() { return tables.stream().mapToInt(Table::getCustomerCount).sum(); }
}
