package com.poseidon.app.services;

import com.poseidon.app.domain.User;
import com.poseidon.app.dto.UserDto;
import com.poseidon.app.exceptions.UserAlreadyExistException;
import com.poseidon.app.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    
    @InjectMocks
    private UserService userService;

    private User user;
    @SuppressWarnings("unused")
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1);
        user.setUsername("john");
        user.setPassword("pass");
        user.setFullname("John Doe");
        user.setRole("TUTOR");

        userDto = new UserDto(1, "john", "pass", "John Doe", "TUTOR");
    }

    
    @Test
    void save_ShouldCreateNewUser_WhenUsernameNotExists() throws UserAlreadyExistException {
        when(userRepository.existsByUsername("john")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDto result = userService.save(new UserDto(null, "john", "pass", "John Doe", "TUTOR"));

        assertEquals("john", result.username());
        verify(userRepository, times(1)).save(any(User.class));
    }
   

    @Test
    void save_ShouldThrowException_WhenUsernameAlreadyExists() {
        when(userRepository.existsByUsername("john")).thenReturn(true);

        UserDto newDto = new UserDto(null, "john", "pass", "John Doe", "TUTOR");

        assertThrows(UserAlreadyExistException.class, () -> userService.save(newDto));
        verify(userRepository, never()).save(any());
    }

   
    @Test
    void getAll_ShouldReturnListOfUsers() {
        when(userRepository.findAll()).thenReturn(List.of(user));

        List<UserDto> result = userService.getAll();

        assertEquals(1, result.size());
        assertEquals("john", result.get(0).username());
        verify(userRepository).findAll();
    }

    
    @Test
    void getUserById_ShouldReturnUser_WhenFound() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        UserDto result = userService.getUserById(1);

        assertEquals("john", result.username());
        verify(userRepository).findById(1);
    }

    @Test
    void getUserById_ShouldThrowException_WhenNotFound() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> userService.getUserById(1));
    }

   
    @Test
    void deleteUserById_ShouldCallRepository() {
        userService.deleteUserById(1);
        verify(userRepository).deleteById(1);
    }

    
    @Test
    void getUserByUsername_ShouldReturnUser_WhenFound() {
        when(userRepository.findByUsername("john")).thenReturn(Optional.of(user));

        User result = userService.getUserByUsername("john");

        assertEquals("john", result.getUsername());
        verify(userRepository).findByUsername("john");
    }

    @Test
    void getUserByUsername_ShouldThrowException_WhenNotFound() {
        when(userRepository.findByUsername("john")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.getUserByUsername("john"));
    }
}

