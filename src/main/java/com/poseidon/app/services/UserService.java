package com.poseidon.app.services;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.poseidon.app.domain.User;
import com.poseidon.app.dto.UserDto;
import com.poseidon.app.exceptions.UserAlreadyExistException;
import com.poseidon.app.repositories.UserRepository;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;
    
    @Autowired
    PasswordEncoder passwordEncoder;
    
    public UserDto save(UserDto userDto) throws UserAlreadyExistException {
        
        User user;
        boolean newUser = (userDto.id() == null);
        if (userDto.id() != null && 
                userRepository.existsById(userDto.id())){
            
            user = userRepository.findById(userDto.id()).orElseThrow();
            
            //Check if another user have this username
            if (userRepository.existsByUsername(userDto.username())){
                
                User userExistByUserName = userRepository.findByUsername(userDto.username()).orElseThrow();
                
                if (userExistByUserName.getId() != userDto.id()) {
                    throw new UserAlreadyExistException("User " + userDto.username() + " already exist");
                }
                
            }
            
            user.setUsername(userDto.username());
            user.setPassword(userDto.password());
            user.setFullname(userDto.fullname());
            user.setRole(userDto.role());
        } else {
            if (newUser && !userRepository.existsByUsername(userDto.username())) {
                user = new User();
                user.setUsername(userDto.username());
                user.setPassword(userDto.password());
                user.setFullname(userDto.fullname());
                user.setRole(userDto.role());
            }else {
                throw new UserAlreadyExistException("User " + userDto.username() + " already exist");
            }
            
            
        }
        
        User saved = userRepository.save(user);
                
        
        return new UserDto(saved.getId(),
                            saved.getUsername(),
                            saved.getPassword(),
                            saved.getFullname(),
                            saved.getRole());
    }
    
    public List<UserDto> getAll(){
        return userRepository.findAll()
                                .stream()
                                .map(c -> {return new UserDto(c.getId(),
                                                                    c.getUsername(),
                                                                    c.getPassword(),
                                                                    c.getFullname(),
                                                                    c.getRole());})
                                                            .collect(Collectors.toList());
    }
    
    public UserDto getUserById(Integer id) {
        User user = userRepository.findById(id)
                                .orElseThrow(() -> new IllegalArgumentException("Invalid User Id:" + id));
        
        return new UserDto(  user.getId(),
                                   user.getUsername(), 
                                   user.getPassword(),
                                   user.getFullname(),
                                   user.getRole());
                                   
    }
    
    public void deleteUserById(Integer id) {
        userRepository.deleteById(id);
    }
    
    public User getUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                    .orElseThrow(() ->  new UsernameNotFoundException(String.format("User with username {%s} not found", username)));
    }
    
    public User getUserByUsernameElseCreateUserIfOAuth2(String username, String fullName)  {
        return userRepository.findByUsername(username)
                                .orElseGet(() -> {User newUser = new User(null, 
                                        username, 
                                        passwordEncoder.encode("NopasswordOAuth2"),
                                        fullName, 
                                        "USER");
                                return userRepository.save(newUser);
                                });
    }
    
}
