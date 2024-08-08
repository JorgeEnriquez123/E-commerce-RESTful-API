package com.jorge.ecommerce.configuration.security;

import com.jorge.ecommerce.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.of;

@Component
@RequiredArgsConstructor
@Slf4j
public class SecurityManager {
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public Optional<Authentication> authenticate(String jwt){
        log.debug("Authenticating jwt: {}", jwt);
        try {
            var username = jwtUtil.extractUsername(jwt);
            if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){
                var userAuthenticated = userDetailsService.loadUserByUsername(username);
                log.debug("User authenticated: {}", userAuthenticated);
                if(jwtUtil.isTokenValid(jwt, userAuthenticated)){
                    return of(new UsernamePasswordAuthenticationToken(
                            userAuthenticated,
                            null,
                            new ArrayList<>()));
                }
            }
        }
        catch (UsernameNotFoundException ex) {
            log.error("User not found: {}", ex.getMessage());
        } catch (Exception ex){
            log.error("JWT token could not be validated. {}", ex.getMessage());
        }
        return empty();
    }
}
