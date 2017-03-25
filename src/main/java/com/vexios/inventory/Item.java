package com.vexios.inventory;

/**
 * Created by Liberato Camilleri on 25/03/2017.
 */
public class Item {

    private long id;
    private String name;
    private String description;
    private int count;
    private long timestamp;

    public Item(final long id, final String name, final String description, final int count, final long timestamp) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.count = count;
        this.timestamp = timestamp;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getCount() {
        return count;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
