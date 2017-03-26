package com.vexios.inventory.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorInfo {

    private String field;

    @NotNull
    private String message;

    public void setField(final String field) {
        this.field = field;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public String getField() {
        return field;
    }

    public String getMessage() {
        return message;
    }
}
