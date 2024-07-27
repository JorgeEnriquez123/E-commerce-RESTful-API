package com.jorge.ecommerce.handler.security;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
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
        response.getWriter().write("{\"message\":\"" + authException.getMessage() + "\"}");
    }
}
