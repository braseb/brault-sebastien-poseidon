package com.poseidon.app.security;

import java.io.IOException;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import com.poseidon.app.domain.User;
import com.poseidon.app.dto.UserDto;
import com.poseidon.app.services.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Handles successful authentication for both standard username/password and OAuth2 logins.
 * 
 * <p>This handler is responsible for:
 * <ul>
 *     <li>Creating or updating the user in the database in case of OAuth2 login.</li>
 *     <li>Generating a JWT token for the authenticated user.</li>
 *     <li>Setting the JWT token in an HTTP-only cookie for stateless authentication.</li>
 *     <li>Clearing any existing JSESSIONID cookie to enforce stateless behavior.</li>
 *     <li>Redirecting the user to the appropriate page based on their role (ADMIN or USER).</li>
 * </ul>
 * 
 * <p>Security considerations:
 * <ul>
 *     <li>The JWT token is stored in a HttpOnly cookie to mitigate XSS attacks.</li>
 *     <li>The application is stateless; CSRF protection is disabled.</li>
 * </ul>
 * 
 * <p>Usage:
 * This class is automatically invoked by Spring Security after a successful authentication.
 * It supports both standard form login and OAuth2 login (e.g., Google OAuth2).
 */

@Component
public class SuccesLoginHandler implements AuthenticationSuccessHandler {
    
        
    
    private final JwtService jwtService;
    private final UserService userService;
       
    /**
     * Constructs a SuccesLoginHandler with required dependencies.
     * 
     * @param jwtService the service responsible for creating JWT tokens
     * @param userService the service responsible for user management
     */
    
    public SuccesLoginHandler(JwtService jwtService,
                              UserService userService
                              
                             ) {
        this.jwtService = jwtService;
        this.userService = userService;
        }
    
    /**
     * Called when a user has been successfully authenticated.
     * 
     * <p>If the authentication is via OAuth2:
     * <ul>
     *     <li>Extracts the user's email as username and full name from the OAuth2 attributes.</li>
     *     <li>Creates a new user in the database if they do not already exist.</li>
     *     <li>Updates the user's full name in the database if it has changed.</li>
     * </ul>
     * 
     * <p>If the authentication is standard username/password:
     * <ul>
     *     <li>Retrieves the user by username from the database.</li>
     * </ul>
     * 
     * <p>After retrieving or creating the user:
     * <ul>
     *     <li>Generates a JWT token and sets it in an HTTP-only cookie.</li>
     *     <li>Removes any existing JSESSIONID cookie to enforce stateless authentication.</li>
     *     <li>Redirects the user to the appropriate page based on their role.</li>
     * </ul>
     *
     * @param request the HttpServletRequest
     * @param response the HttpServletResponse
     * @param authentication the Authentication object containing user details
     * @throws IOException if an input/output error occurs during redirect
     * @throws ServletException if a servlet-specific error occurs
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
               
        User user = null;
        
        if(authentication instanceof OAuth2AuthenticationToken){
                       
            Map<String,Object> userAttributes = ((DefaultOAuth2User) authentication.getPrincipal()).getAttributes();
                                    
            String userName = (String) userAttributes.get("email");
            String fullName = (String) userAttributes.get("name");
            
            /*Create user in database if it not exist*/
            user = userService.getUserByUsernameElseCreateUserIfOAuth2(userName, fullName);
                                
            // update fullName if necessary
            if (!user.getFullname().equals(fullName)){
                user.setFullname(fullName);
                userService.save(new UserDto(user.getId(),
                                            user.getUsername(),
                                            user.getPassword(),
                                            user.getFullname(),
                                            user.getRole()));
            }
                  
        
        
        }else {
            String userName = authentication.getName();
            user = userService.getUserByUsername(userName);
            
        } 
        String token = jwtService.createToken(user);
        
        
        Cookie cookie = new Cookie("JWT_TOKEN", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        //cookie.setSecure(true); // activate in production
        cookie.setMaxAge(3600);
        response.addCookie(cookie);
        
        
        Cookie cookieSession = new Cookie("JSESSIONID", null);
        cookieSession.setHttpOnly(true);
        cookieSession.setPath("/");
        //cookie.setSecure(true); // activate in production
        cookieSession.setMaxAge(0);
        response.addCookie(cookieSession);     
        
        if (user.getRole().equals("ADMIN")) {
            response.sendRedirect("/app/secure/article-details");
        }else if (user.getRole().equals("USER")) {
            response.sendRedirect("/curvePoint/list");
        }else {
            response.sendRedirect("/app/error");
        }
      
       
    }

}
