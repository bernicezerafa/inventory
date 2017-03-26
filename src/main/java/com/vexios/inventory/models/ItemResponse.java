package com.vexios.inventory.models;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLong;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ItemResponse implements Serializable {

    private static final AtomicLong autoGenerator = new AtomicLong();

    private long id;
    private String name;
    private String description;
    private Integer count;
    private long timestamp;

    public ItemResponse() {
        id = autoGenerator.incrementAndGet();
        timestamp = System.currentTimeMillis();
    }

    public long getId() {
        return id;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setCount(final Integer count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
