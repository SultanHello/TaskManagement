package org.example.taskmanagement.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.taskmanagement.dto.LogUser;
import org.example.taskmanagement.dto.RegUser;
import org.example.taskmanagement.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Login and register for users")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Add a new user to the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User is created"),
            @ApiResponse(responseCode = "400", description = "Bad request, wrong data"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    public ResponseEntity<String> register(@RequestBody RegUser registerUser) {
        try {
            String result = authService.register(registerUser);
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error server: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    @Operation(summary = "Login user", description = "Login with email and password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login OK, token returned"),
            @ApiResponse(responseCode = "401", description = "Wrong email or password"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    public ResponseEntity<String> login(@RequestBody LogUser loginUser) {
        try {
            String token = authService.login(loginUser);
            return ResponseEntity.ok(token);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Authentication error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error server: " + e.getMessage());
        }
    }
}