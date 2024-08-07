package com.jorge.ecommerce.handler;

import com.jorge.ecommerce.handler.exception.*;
import com.jorge.ecommerce.handler.response.ErrorResponse;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
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
@Slf4j
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorResponse internalServerErrorHandler(Exception ex) {
        log.error("Internal Server Error");
        log.debug("Exception Details: ", ex);

        return ErrorResponse.builder()
                .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .message("Internal Error")
                .errors(Collections.singletonList(ex.getMessage()))
                .build();
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(FailedLoginException.class)
    public ErrorResponse failedLoginExceptionHandler(FailedLoginException ex) {
        log.error("Unsuccessful Login");
        log.debug("Exception Details: ", ex);

        return ErrorResponse.builder()
                .httpStatus(HttpStatus.UNAUTHORIZED)
                .message("Unsuccessful Login")
                .errors(Collections.singletonList(ex.getMessage()))
                .build();

    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(FailedRefreshTokenException.class)
    public ErrorResponse failedRefreshTokenExceptionHandler(FailedRefreshTokenException ex) {
        log.error("Failed Refresh Token");
        log.debug("Exception Details: ", ex);

        return ErrorResponse.builder()
                .httpStatus(HttpStatus.BAD_REQUEST)
                .message("Failed Refresh Token")
                .errors(Collections.singletonList(ex.getMessage()))
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ErrorResponse dataIntegrityViolationExceptionHandler(DataIntegrityViolationException ex) {
        log.error("Data Integrity Violation", ex);
        log.debug("Exception Details: ", ex);

        return ErrorResponse.builder()
                .httpStatus(HttpStatus.BAD_REQUEST)
                .message("Data Integrity Violation")
                .errors(Collections.singletonList("Data Integrity Violation"))
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ErrorResponse sqlIntegrityConstraintViolationExceptionHandler(SQLIntegrityConstraintViolationException ex) {
        log.error("Integrity Constraint Violation");
        log.debug("Exception Details: ", ex);

        return ErrorResponse.builder()
                .httpStatus(HttpStatus.BAD_REQUEST)
                .message("Integrity Constraint Violation")
                .errors(Collections.singletonList("Integrity Constraint Violation"))
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ErrorResponse httpMessageNotReadableExceptionHandler(HttpMessageNotReadableException ex) {
        log.error("Invalid JSON");
        log.debug("Exception Details: ", ex);

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
        log.error("Validation Failed {}", errors);
        log.debug("Exception Details: ", ex);

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
        log.error("Validation Failed: {}", errors);
        log.debug("Exception details: ", ex);

        return ErrorResponse.builder()
                .httpStatus(HttpStatus.BAD_REQUEST)
                .message("Validation failed")
                .errors(errors)
                .build();
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ErrorResponse ResourceNotFoundExceptionHandler(ResourceNotFoundException ex){
        log.error("Resource not found");
        log.debug("Exception Details: ", ex);

        return ErrorResponse.builder()
                .httpStatus(HttpStatus.NOT_FOUND)
                .message("Resource not found")
                .errors(Collections.singletonList(ex.getMessage()))
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ValueAlreadyExistsException.class)
    public ErrorResponse ValueAlreadyExistsExceptionHandler(ValueAlreadyExistsException ex){
        log.error("Value Already exists");
        log.debug("Exception Details: ", ex);

        return ErrorResponse.builder()
                .httpStatus(HttpStatus.BAD_REQUEST)
                .message("Value already exists")
                .errors(Collections.singletonList(ex.getMessage()))
                .build();
    }
}
