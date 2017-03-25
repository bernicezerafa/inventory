package com.vexios.inventory;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

@JsonIgnoreProperties
public class Item implements Serializable {

    private static final AtomicLong autoGenerator = new AtomicLong();

    private long id;

    @NotNull
    @Size(min = 2, max = 50)
    private String name;

    @NotNull
    @Size(max = 150)
    private String description;

    @NotNull
    @Min(1)
    @Max(100)
    private Integer count;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date timestamp;

    public Item() {
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
