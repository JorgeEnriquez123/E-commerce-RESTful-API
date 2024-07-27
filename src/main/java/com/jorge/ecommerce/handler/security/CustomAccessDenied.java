package com.jorge.ecommerce.handler.security;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

@Slf4j
public class CustomAccessDenied implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        String requestURI = (String) request.getAttribute(RequestDispatcher.FORWARD_REQUEST_URI);
        if (requestURI == null) {
            requestURI = request.getRequestURI();
        }
        log.warn("Error processing request: HTTP Method = {}, URL = {}, Error = {}",
                request.getMethod(),
                requestURI,
                accessDeniedException.getMessage()
        );

        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType("application/json");
        response.getWriter().write("{\"message\":\"" + accessDeniedException.getMessage() + "\"}");
    }
}
