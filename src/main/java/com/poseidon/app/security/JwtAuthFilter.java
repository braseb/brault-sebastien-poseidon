package com.poseidon.app.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter{
    
    @Autowired
    private JwtService jwtService;
 

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        //String header = request.getHeader("Authorization");
        
        //if (header != null && header.startsWith("Bearer ")) {
        //    String token = header.substring(7);
        String token = extractCookie(request, "JWT_TOKEN");
                
        if (token != null && jwtService.validateToken(token)) {
                String username = jwtService.extractUsername(token);
                String role = jwtService.extractRole(token);
                System.out.println("role : " + jwtService.extractRole(token));
                if (role == null || role.isBlank()) {
                    role = "ROLE_AUCUN";
                }
                
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        new User(username, "", List.of(new SimpleGrantedAuthority(role))),
                        null,
                        List.of(new SimpleGrantedAuthority(role))
                );
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        
        chain.doFilter(request, response);
    }
    
    private String extractCookie(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(cookieName)) {
                return cookie.getValue();
            }
        }
        return null;
    }

    
        
        
}
