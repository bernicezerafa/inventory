package com.vexios.inventory.errors;

public class NotFoundException extends RuntimeException {

    private String message;

    public NotFoundException(final String message) {
        super(message);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
