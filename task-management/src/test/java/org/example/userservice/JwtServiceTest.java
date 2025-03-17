package org.example.userservice;

import org.example.userservice.service.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class JwtServiceTest {

    private final JwtService jwtService = new JwtService();

    @Test
    void generateToken_success() {
        String token = jwtService.generateToken("test@mail.com");
        assertNotNull(token);
        assertEquals("test@mail.com", jwtService.extractUsername(token));
    }



}