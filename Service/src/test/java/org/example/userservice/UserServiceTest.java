package org.example.userservice;

import org.example.userservice.dto.UpdateUserDTO;
import org.example.userservice.dto.UserDTO;
import org.example.userservice.factory.UserFactory;
import org.example.userservice.model.User;
import org.example.userservice.repository.UserRepository;
import org.example.userservice.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserFactory userFactory;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void findByEmail_success() {
        User user = User.builder().email("test@mail.com").build();
        when(userRepository.findByEmail("test@mail.com")).thenReturn(Optional.of(user));

        User result = userService.findByEmail("test@mail.com");

        assertEquals(user, result);
    }

    @Test
    void addUser_success() {
        UserDTO userDTO = new UserDTO("test@mail.com", "password", null);
        User user = User.builder().email("test@mail.com").build();

        when(userRepository.findByEmail("test@mail.com")).thenReturn(Optional.empty());
        when(userFactory.create(userDTO)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);

        userService.addUser(userDTO);

        verify(userRepository).save(user);
    }

    @Test
    void updateUser_success() {
        User user = User.builder().id(1L).email("old@mail.com").password("oldPass").build();
        UpdateUserDTO updateDTO = new UpdateUserDTO("new@mail.com", "newPass");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode("newPass")).thenReturn("encodedNewPass");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userService.updateUser(1L, updateDTO);

        assertEquals("new@mail.com", result.getEmail());
        assertEquals("encodedNewPass", result.getPassword());
    }
}