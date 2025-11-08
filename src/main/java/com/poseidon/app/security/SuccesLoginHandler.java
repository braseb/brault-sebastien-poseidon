package com.poseidon.app.security;

import java.io.IOException;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.poseidon.app.domain.User;
import com.poseidon.app.repositories.UserRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SuccesLoginHandler implements AuthenticationSuccessHandler {
    
        
    
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final OAuth2AuthorizedClientService authorizedClientService;
    private final PasswordEncoder passwordEncoder;
   

    public SuccesLoginHandler(JwtService jwtService,
                              UserRepository userRepository,
                              OAuth2AuthorizedClientService authorizedClientService,
                              PasswordEncoder passwordEncoder) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.authorizedClientService = authorizedClientService;
        this.passwordEncoder = passwordEncoder;
    }
    
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        
        
        System.out.println("principal : " + authentication.getPrincipal().toString());
        System.out.println((authentication instanceof OAuth2AuthenticationToken));
        System.out.println(authentication.getClass().getName());
        User user = null;
        
        if(authentication instanceof OAuth2AuthenticationToken){
            
            OAuth2AuthenticationToken authToken = ((OAuth2AuthenticationToken) authentication);
            OAuth2AuthorizedClient authClient = this.authorizedClientService.loadAuthorizedClient(authToken.getAuthorizedClientRegistrationId(), authToken.getName());
            Map<String,Object> userAttributes = ((DefaultOAuth2User) authentication.getPrincipal()).getAttributes();
            String userToken = authClient.getAccessToken().getTokenValue();

            System.out.println(userToken);
            System.out.println(userAttributes.toString());
                        
            String userName = (String) userAttributes.get("email");
            System.out.println("userName : " + userName);
            
            String fullName = (String) userAttributes.get("name");
            System.out.println("fullName : " + fullName);
            
            /*Create user in database if it not exist*/
            user = userRepository.findByUsername(userName)
                                .orElseGet(() -> {User newUser = new User(null, 
                                                            userName, 
                                                            passwordEncoder.encode("NopasswordOAuth2"),
                                                            fullName, 
                                                            "USER");
                                                    return userRepository.save(newUser);
                                                    });
            // update fullName if necessary
            if (!user.getFullname().equals(fullName)){
                user.setFullname(fullName);
                userRepository.save(user);
            }
                  
        
        
        }else {
            String userName = authentication.getName();
            user = userRepository.findByUsername(userName)
                    .orElseThrow(() -> new UsernameNotFoundException("Unable to find user"));
            
        } 
        String token = jwtService.createToken(user);
        
        
        Cookie cookie = new Cookie("JWT_TOKEN", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        //cookie.setSecure(true);
        cookie.setMaxAge(3600);
        response.addCookie(cookie);
        
        
        Cookie cookieSession = new Cookie("JSESSIONID", null);
        cookieSession.setHttpOnly(true);
        cookieSession.setPath("/");
        //cookie.setSecure(true);
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
