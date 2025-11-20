package com.poseidon.app.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.poseidon.app.exceptions.RoleNotFoundException;

import java.io.IOException;
import java.util.List;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
* JWT authentication filter that validates JWT tokens for each HTTP request.
*
* <p>This filter extends {@link OncePerRequestFilter} and is invoked once per request.
* It extracts the JWT token from an HTTP-only cookie named "JWT_TOKEN", validates it,
* and sets the {@link org.springframework.security.core.Authentication} in the
* {@link SecurityContextHolder} if the token is valid.</p>
*
* <p>If the JWT token is invalid or the role is missing, authentication is not set,
* and a {@link com.poseidon.app.exceptions.RoleNotFoundException} is thrown in case
* of missing role.</p>
*
* <p>Security context is updated with a {@link UsernamePasswordAuthenticationToken} containing
* the username and role extracted from the token.</p>
*/

@Component
public class JwtAuthFilter extends OncePerRequestFilter{
    
    @Autowired
    private JwtService jwtService;
    
    /**
     * Filters each HTTP request to validate JWT authentication.
     *
     * <p>This method is invoked once per request. It performs the following steps:
     * <ul>
     *     <li>Extracts the JWT token from the "JWT_TOKEN" cookie.</li>
     *     <li>Validates the token using the {@link JwtService}.</li>
     *     <li>Extracts the username and role from the token.</li>
     *     <li>Throws {@link com.poseidon.app.exceptions.RoleNotFoundException} if role is missing.</li>
     *     <li>Sets {@link UsernamePasswordAuthenticationToken} in {@link SecurityContextHolder}.</li>
     * </ul>
     * </p>
     *
     * @param request the HTTP request
     * @param response the HTTP response
     * @param chain the filter chain
     * @throws ServletException if a servlet exception occurs
     * @throws IOException if an input or output exception occurs
     * @throws com.poseidon.app.exceptions.RoleNotFoundException if JWT role is missing or blank
     */
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException, RoleNotFoundException {

        
        String token = extractCookie(request, "JWT_TOKEN");
                
        if (token != null && jwtService.validateToken(token)) {
                String username = jwtService.extractUsername(token);
                String role = jwtService.extractRole(token);
                System.out.println("role : " + jwtService.extractRole(token));
                if (role == null || role.isBlank()) {
                    throw new RoleNotFoundException("Role not found");
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
    
    /**
     * Extracts a cookie value from the HTTP request by its name.
     *
     * @param request the HTTP request
     * @param cookieName the name of the cookie to extract
     * @return the cookie value if found, otherwise null
     */
    
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
