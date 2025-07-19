package com.example.jobtracker.service;

import com.example.jobtracker.dto.AuthRequest;
import com.example.jobtracker.dto.AuthResponseDTO;
import com.example.jobtracker.dto.UserResponseDTO;
import com.example.jobtracker.model.User;
import com.example.jobtracker.repository.UserRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder
    , JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public AuthResponseDTO authenticateUser(AuthRequest authRequest) {
        User user = userRepository.findByUsername(authRequest.getUsername());
        if (user != null && passwordEncoder.matches(authRequest.getPassword(), user.getPassword())) {
            String jwt = jwtUtil.generateToken(user.getUsername());
            return new AuthResponseDTO(jwt);
        }
        throw new BadCredentialsException("Invalid username or password");
    }

    public UserResponseDTO registerUser(AuthRequest authRequst){
        User user = userRepository.findByUsername(authRequst.getUsername());
        if (user != null) {
            throw new BadCredentialsException("username already exists");
        }

        String hashedPassword = passwordEncoder.encode(authRequst.getPassword());
        User newUser = new User();

        newUser.setUsername(authRequst.getUsername());
        newUser.setPassword(hashedPassword);
        userRepository.save(newUser);
        return new UserResponseDTO(newUser.getId(), newUser.getUsername());
    }
}
