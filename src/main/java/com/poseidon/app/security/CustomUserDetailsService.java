package com.poseidon.app.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.poseidon.app.domain.User;
import com.poseidon.app.services.UserService;
/**
 * CustomUserDetailsService is responsible for loading user-specific data during authentication
 * 
 * It implements the Spring Security {@link UserDetailsService} interface
 * which defines the method to retrieve user details based on the username provided during login 
 * 
 * CustomUserDetailsService is used to verify the existence of a user in database and
 * provides the user's credentials and roles for authentication.
 * 
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    UserService userService;
    
    /**
     * Loads a user based on their username. 
     * This method is called by Spring Security during the authentication process
     * 
     * It searchs the user in the database,
     * converts the {@link User} entity into a {@link UserDetails} object
     * and assigns the appropriate role to the user for authorization
     * 
     * @param username the username of the user attempting to authenticate
     * @return a {@link UserDetails} object containing the user's credentials and roles 
     * @throws UsernameNotFoundException if no user with the given name is found 
     * @see UserService#getUserByUsername(String) 
     */
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                
        User user = userService.getUserByUsername(username);
                
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole())
                .build();
        
        }
}
