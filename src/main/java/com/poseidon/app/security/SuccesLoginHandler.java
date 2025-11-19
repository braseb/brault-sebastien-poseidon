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

@Component
public class SuccesLoginHandler implements AuthenticationSuccessHandler {
    
        
    
    private final JwtService jwtService;
    private final UserService userService;
    //private final PasswordEncoder passwordEncoder;
   

    public SuccesLoginHandler(JwtService jwtService,
                              UserService userService
                              
                             ) {
        this.jwtService = jwtService;
        this.userService = userService;
        }
    
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
