package org.example.userservice;

import org.example.userservice.dto.LogUser;
import org.example.userservice.dto.RegUser;
import org.example.userservice.model.Role;
import org.example.userservice.model.User;
import org.example.userservice.repository.UserRepository;
import org.example.userservice.service.AuthService;
import org.example.userservice.service.JwtService;
import org.example.userservice.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthService authService;

    private RegUser regUser;
    private LogUser logUser;

    @BeforeEach
    void setUp() {
        regUser = new RegUser("test@mail.com", "password", Role.USER);
        logUser = new LogUser("test@mail.com", "password");
    }

    @Test
    void register_success() {
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(User.builder().email("test@mail.com").build());
        when(jwtService.generateToken("test@mail.com")).thenReturn("jwtToken");

        String token = authService.register(regUser);

        assertEquals("jwtToken", token);
        verify(userRepository).save(any(User.class));
        verify(jwtService).generateToken("test@mail.com");
    }

    @Test
    void login_success() {
        User user = User.builder().email("test@mail.com").password("encodedPassword").build();
        when(userService.findByEmail("test@mail.com")).thenReturn(user);
        when(passwordEncoder.matches("password", "encodedPassword")).thenReturn(true);
        when(jwtService.generateToken("test@mail.com")).thenReturn("jwtToken");

        String token = authService.login(logUser);

        assertEquals("jwtToken", token);
        verify(userService).findByEmail("test@mail.com");
    }

    @Test
    void login_userNotFound_throwsException() {
        when(userService.findByEmail("test@mail.com")).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> authService.login(logUser));
    }

    @Test
    void login_invalidPassword_throwsException() {
        User user = User.builder().email("test@mail.com").password("encodedPassword").build();
        when(userService.findByEmail("test@mail.com")).thenReturn(user);
        when(passwordEncoder.matches("password", "encodedPassword")).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> authService.login(logUser));
    }
}