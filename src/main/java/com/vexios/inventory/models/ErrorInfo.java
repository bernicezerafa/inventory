package com.vexios.inventory.models;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.NotNull;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
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
