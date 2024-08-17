package com.jorge.ecommerce.configuration.security;

import com.jorge.ecommerce.jwt.JwtUtil;
import com.jorge.ecommerce.model.AuthenticatedUserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

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
        log.debug("Authenticating jwt");
        try {
            var username = jwtUtil.extractUsername(jwt);
            if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){
                var userAuthenticated = userDetailsService.loadUserByUsername(username);

                // To avoid logging sensitive information
                AuthenticatedUserDto authDto = AuthenticatedUserDto.builder()
                        .username(userAuthenticated.getUsername())
                        .roles(userAuthenticated.getAuthorities().stream()
                                .map(GrantedAuthority::getAuthority)
                                .toList())
                        .build();

                log.debug("User authenticated: {}", authDto);
                // ---

                if(jwtUtil.isTokenValid(jwt, userAuthenticated)){
                    log.debug("JWT succesfully validated for user with username: {}", authDto.getUsername());
                    return of(new UsernamePasswordAuthenticationToken(
                            userAuthenticated,
                            null,
                            userAuthenticated.getAuthorities()));
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
