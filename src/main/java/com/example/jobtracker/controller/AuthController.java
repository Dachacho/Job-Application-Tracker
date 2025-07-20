package com.example.jobtracker.controller;

import com.example.jobtracker.dto.AuthRequest;
import com.example.jobtracker.dto.AuthResponseDTO;
import com.example.jobtracker.dto.RegisterRequest;
import com.example.jobtracker.dto.UserResponseDTO;
import com.example.jobtracker.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<UserResponseDTO> register(@RequestBody RegisterRequest registerRequest) {
        UserResponseDTO userResponseDTO = authService.registerUser(registerRequest);
        return ResponseEntity.ok(userResponseDTO);
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verifyUser(@RequestParam("token") String token) {
        String verificationResponse = authService.verifyEmail(token);
        if (verificationResponse.equals("Success")) {
            return ResponseEntity.ok(verificationResponse);
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam("email") String email) {
        String response = authService.forgotPassword(email);
        if (response.equals("Reset link sent to your email")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(
            @RequestParam("token") String token,
            @RequestParam("newPassword") String newPassword) {
        String response = authService.resetPassword(token, newPassword);
        if (response.equals("Password reset successful")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }
}
