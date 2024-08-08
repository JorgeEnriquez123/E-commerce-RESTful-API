package com.jorge.ecommerce.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Slf4j
@Component
public class JwtUtil {
    @Value("${application.jwt.key}")
    private String SECRET_KEY;

    @Value("${application.jwt.expiration}")
    private long jwtExpiration;
    @Value("${application.jwt.refresh-token.expiration}")
    private long refreshJwtExpiration;

    public String generateToken(UserDetails user) {
        return generateToken(new HashMap<>(), user);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails user) {
        log.debug("Generating token");
        return buildToken(extraClaims, user, jwtExpiration);
    }

    public String generateRefreshToken(UserDetails user) {
        log.debug("Generating refresh token");
        return buildToken(new HashMap<>(), user, refreshJwtExpiration);
    }

    private String buildToken(Map<String, Object> claims, UserDetails user, long expiration) {
        log.debug("Building token");
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Claims extractAllClaims(String token){
        log.debug("Extracting all claims from token: {}", token);
        return Jwts.parserBuilder()
                    .setSigningKey(getKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String extractUsername(String token) {
        log.debug("Extracting username from token: {}", token);
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        log.debug("Extracting expiration from token: {}", token);
        return extractClaim(token, Claims::getExpiration);
    }

    private boolean isTokenExpired(String token){
        log.debug("Checking if token has expired: {}", token);
        return extractExpiration(token).before(new Date(System.currentTimeMillis()));
    }

    public boolean isTokenValid(String token, UserDetails user){
        log.debug("Checking if token is valid: {}", token);
        var username = extractUsername(token);
        return (username.equals(user.getUsername()) && !isTokenExpired(token));
    }

    private Key getKey() {
        log.debug("Decoding secret key");
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
