package com.poseidon.app.security;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;


import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Logout success handler.
 *
 * <p>This class handles successful logout by clearing the JWT token cookie
 * and redirecting the user to the login page.</p>
 */

@Component
public class SuccesLogoutHandler implements LogoutSuccessHandler {
    
    /**
     * Called on successful logout.
     *
     * <p>Removes the JWT token cookie and redirects to the login page
     * with a logout flag.</p>
     *
     * @param request the HTTP request
     * @param response the HTTP response
     * @param authentication the current Authentication object (may be null)
     * @throws IOException if an input or output exception occurs
     * @throws ServletException if a servlet exception occurs
     */
    
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
                
        Cookie cookie = extractCookie(request, "JWT_TOKEN");
        if (cookie != null) {
            cookie.setValue(null);
            cookie.setMaxAge(0);
            cookie.setPath("/");
            response.addCookie(cookie);
        }
        
        response.sendRedirect("/app/login?logout=true");    
    }
    
    /**
     * Extracts a cookie from the request by name.
     *
     * @param request the HTTP request
     * @param cookieName the name of the cookie
     * @return the Cookie object if found, otherwise null
     */
    
    private Cookie extractCookie(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(cookieName)) {
                return cookie;
            }
        }
        return null;
    }

}
