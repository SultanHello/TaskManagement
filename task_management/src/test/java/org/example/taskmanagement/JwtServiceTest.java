package org.example.taskmanagement;

import org.example.taskmanagement.service.JwtService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class JwtServiceTest {

    private final JwtService jwtService = new JwtService();

    @Test
    void generateToken_success() {
        String token = jwtService.generateToken("test@mail.com");
        assertNotNull(token);
        assertEquals("test@mail.com", jwtService.extractUsername(token));
    }



}