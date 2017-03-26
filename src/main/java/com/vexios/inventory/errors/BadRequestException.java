package com.vexios.inventory.errors;

import org.springframework.validation.Errors;

/**
 * Exception thrown when a 400 response should be thrown from APIs.
 */
public class BadRequestException extends RuntimeException {

    private Errors errors;

    public BadRequestException(final Errors errors) {
        this.errors = errors;
    }

    public Errors getErrors() { return errors; }
}