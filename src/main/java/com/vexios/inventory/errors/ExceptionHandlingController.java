package com.vexios.inventory.errors;

import com.vexios.inventory.models.ErrorInfo;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

import java.util.stream.Collectors;

@ControllerAdvice
public class ExceptionHandlingController {

    private static final String GENERAL_ERROR = "Something went wrong while processing your request. Please try again later.";

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({BadRequestException.class})
    public @ResponseBody
    List<ErrorInfo> handleBadRequest(final BadRequestException e) {
        return e.getErrors().getFieldErrors()
                .stream()
                .map(error -> buildErrorInfo(error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler({IllegalStateException.class})
    public @ResponseBody
    ErrorInfo handleLogicalError(final IllegalStateException e) {
        return buildErrorInfo(null, e.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({NotFoundException.class})
    public @ResponseBody
    ErrorInfo handleNotFound(final NotFoundException e) {
        return buildErrorInfo(null, e.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({Exception.class})
    public @ResponseBody
    ErrorInfo handleInternalServerError(final Exception e) {
        return buildErrorInfo(null, GENERAL_ERROR);
    }

    private ErrorInfo buildErrorInfo(final String field, final String message) {
        final ErrorInfo errorInfo = new ErrorInfo();
        if (field != null) {
            errorInfo.setField(field);
        }
        errorInfo.setMessage(message);
        return errorInfo;
    }
}