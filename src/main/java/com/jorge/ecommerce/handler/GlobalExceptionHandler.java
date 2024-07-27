package com.jorge.ecommerce.handler;

import com.jorge.ecommerce.handler.exception.EntityNotFoundException;
import com.jorge.ecommerce.handler.exception.FailedLoginException;
import com.jorge.ecommerce.handler.exception.ValueAlreadyExistsException;
import com.jorge.ecommerce.handler.response.ErrorResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(FailedLoginException.class)
    public ErrorResponse failedLoginExceptionHandler(FailedLoginException ex) {
        return ErrorResponse.builder()
                .httpStatus(HttpStatus.UNAUTHORIZED)
                .message("Unsuccessful Login")
                .errors(Collections.singletonList(ex.getMessage()))
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ErrorResponse sqlIntegrityConstraintViolationExceptionHandler(SQLIntegrityConstraintViolationException ex) {
        return ErrorResponse.builder()
                .httpStatus(HttpStatus.BAD_REQUEST)
                .message("Integrity Constraint Violation")
                .errors(Collections.singletonList(ex.getMessage()))
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ErrorResponse httpMessageNotReadableExceptionHandler(HttpMessageNotReadableException ex) {
        return ErrorResponse.builder()
                .httpStatus(HttpStatus.BAD_REQUEST)
                .message("Invalid JSON")
                .errors(Collections.singletonList(ex.getMessage()))
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ErrorResponse constraintViolationExceptionHandler(ConstraintViolationException ex) {
        List<String> errors = ex.getConstraintViolations().stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .collect(Collectors.toList());
        return ErrorResponse.builder()
                .httpStatus(HttpStatus.BAD_REQUEST)
                .message("Validation failed")
                .errors(errors)
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException ex) {
        List<String> errors = new ArrayList<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        });
        return ErrorResponse.builder()
                .httpStatus(HttpStatus.BAD_REQUEST)
                .message("Validation failed")
                .errors(errors)
                .build();
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EntityNotFoundException.class)
    public ErrorResponse EntityNotFoundExceptionHandler(EntityNotFoundException ex){
        return ErrorResponse.builder()
                .httpStatus(HttpStatus.NOT_FOUND)
                .message("Entity not found")
                .errors(Collections.singletonList(ex.getMessage()))
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ValueAlreadyExistsException.class)
    public ErrorResponse ValueAlreadyExistsExceptionHandler(ValueAlreadyExistsException ex){
        return ErrorResponse.builder()
                .httpStatus(HttpStatus.BAD_REQUEST)
                .message("Value already exists")
                .errors(Collections.singletonList(ex.getMessage()))
                .build();
    }
}
