package com.poseidon.app.security;

import java.security.Key;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.poseidon.app.domain.User;

import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;

@Service
public class JwtService {
    
    
    private final SecretKey key;
        
   
    
    public JwtService(@Value("${jwt.secret}") String jwtSecret) {
        this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }
    
    public String createToken(User user) {
        Date now = new Date();
        
        
        System.out.println("Création du token pour " + user.getUsername() + " avec rôle : " + user.getRole());

        return Jwts.builder()
                .claim("role", "ROLE_" + user.getRole())
                .subject(user.getUsername())
                .issuedAt(now)
                .expiration(new Date(now.getTime() + 60 * 10 * 1000))
                .signWith(key, Jwts.SIG.HS256)
                .compact();
                
    }
    
    public String extractUsername(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
    
    public String extractRole(String token){
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("role", String.class);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
