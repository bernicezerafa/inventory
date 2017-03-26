package com.vexios.inventory.models;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ItemResponse implements Serializable {

    private static final AtomicLong autoGenerator = new AtomicLong();

    private long id;
    private String name;
    private String description;
    private Integer count;
    private Date timestamp;

    public ItemResponse() {
        id = autoGenerator.incrementAndGet();
        timestamp = new Date();
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

    public Date getTimestamp() {
        return timestamp;
    }
}
