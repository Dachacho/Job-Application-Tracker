package com.example.jobtracker.controller;

import com.example.jobtracker.dto.AuthRequest;
import com.example.jobtracker.dto.AuthResponseDTO;
import com.example.jobtracker.dto.UserResponseDTO;
import com.example.jobtracker.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    //endpoints
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody AuthRequest authRequest) {
        AuthResponseDTO authResponseDTO = authService.authenticateUser(authRequest);
        return ResponseEntity.ok(authResponseDTO);
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@RequestBody AuthRequest authRequest) {
        UserResponseDTO userResponseDTO = authService.registerUser(authRequest);
        return ResponseEntity.ok(userResponseDTO);
    }
}
