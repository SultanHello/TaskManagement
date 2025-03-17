package org.example.userservice.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.example.userservice.dto.LogUser;
import org.example.userservice.dto.RegUser;
import org.example.userservice.model.User;
import org.example.userservice.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {
    private final JwtService jwtService;

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;
    private final UserService userService;


    public String register(RegUser regUser) {
//        if (userClient.findByEmail(regUser.getEmail()) != null)
//            throw new IllegalArgumentException("Username is already taken");

        var user = User.builder()
                .email(regUser.getEmail())
                .password(passwordEncoder.encode(regUser.getPassword()))
                .role(regUser.getRole())
                .build();
        userRepository.save(user);
        log.info("User registered: {}", user.getEmail());
        return jwtService.generateToken(user.getEmail());
    }

    public String login(LogUser logUser) {

        var user = userService.findByEmail(logUser.getEmail());
        if (user == null)
            throw new IllegalArgumentException("user not found");

        if (!passwordEncoder.matches(logUser.getPassword(), user.getPassword()))
            throw new IllegalArgumentException("Invalid username or password");

        log.info("User logged in: {}", user.getUsername());
        return jwtService.generateToken(user.getUsername());
    }
}
