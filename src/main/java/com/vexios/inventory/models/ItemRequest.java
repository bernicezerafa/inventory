package com.vexios.inventory.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemRequest implements Serializable {

    @NotBlank
    @Size(max = 50)
    private String name;

    @NotBlank
    @Size(max = 150)
    private String description;

    @NotNull
    @Min(1)
    @Max(100)
    private Integer count;

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
}
