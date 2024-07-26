package com.jorge.ecommerce.handler.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    private HttpStatus httpStatus;
    private String message;
    private List<String> errors;

    public ErrorResponse(HttpStatus httpStatus, String message, String error) {
        this.httpStatus = httpStatus;
        this.message = message;
        this.errors = Arrays.asList(error);
    }
}
