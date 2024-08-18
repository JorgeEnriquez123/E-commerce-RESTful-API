package com.jorge.ecommerce.handler.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jorge.ecommerce.handler.response.ErrorResponse;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.util.Collections;

@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Autowired
    ObjectMapper objectMapper;
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        String requestURI = (String) request.getAttribute(RequestDispatcher.FORWARD_REQUEST_URI);
        if (requestURI == null) {
            requestURI = request.getRequestURI();
        }
        log.warn("Error processing request: HTTP Method = {}, URI = {}, Error = {}",
                request.getMethod(),
                requestURI,
                authException.getMessage()
        );

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");

        ErrorResponse errorResponse = ErrorResponse.builder()
                .httpStatus(HttpStatus.UNAUTHORIZED)
                .message("Unauthorized")
                .errors(Collections.singletonList(authException.getMessage()))
                .build();
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
