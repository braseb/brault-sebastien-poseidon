package com.poseidon.app.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.poseidon.app.domain.User;
import com.poseidon.app.repositories.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;
    
    @Autowired
    private JwtService jwtService;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                
        User user = userRepository.findByUsername(username)
                                .orElseThrow(() ->  new UsernameNotFoundException(String.format("User with username {%s} not found", username)));
                    
        
        
        
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole())
                .build();
        
        }
}
