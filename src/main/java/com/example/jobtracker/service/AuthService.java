package com.example.jobtracker.service;

import com.example.jobtracker.dto.AuthRequest;
import com.example.jobtracker.dto.AuthResponseDTO;
import com.example.jobtracker.dto.RegisterRequest;
import com.example.jobtracker.dto.UserResponseDTO;
import com.example.jobtracker.model.User;
import com.example.jobtracker.repository.UserRepository;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final JavaMailSender javaMailSender;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder
    , JwtUtil jwtUtil,  JavaMailSender javaMailSender) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.javaMailSender = javaMailSender;
    }

    public AuthResponseDTO authenticateUser(AuthRequest authRequest) {
        User user = userRepository.findByUsername(authRequest.getUsername());
        if (user == null || !passwordEncoder.matches(authRequest.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid username or password");
        }
        if (!user.isVerified()) {
            throw new BadCredentialsException("Email not verified. Please check your email for the verification link.");
        }
        String jwt = jwtUtil.generateToken(user.getUsername());
        return new AuthResponseDTO(jwt);
    }

    public UserResponseDTO registerUser(RegisterRequest registerRequest){
        User user = userRepository.findByUsername(registerRequest.getUsername());
        if (user != null) {
            throw new BadCredentialsException("username already exists");
        }

        String hashedPassword = passwordEncoder.encode(registerRequest.getPassword());
        User newUser = new User();

        String emailVerificationToken = UUID.randomUUID().toString();
        String verificationLink = "http://localhost:8080/api/auth/verify?token=" + emailVerificationToken;


        newUser.setUsername(registerRequest.getUsername());
        newUser.setPassword(hashedPassword);
        newUser.setEmail(registerRequest.getEmail());
        newUser.setVerified(false);
        newUser.setEmailVerificationToken(emailVerificationToken);
        userRepository.save(newUser);

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(newUser.getEmail());
        mailMessage.setSubject("Verification your Email");
        mailMessage.setText("Click the link to verify your email: " + verificationLink);
        javaMailSender.send(mailMessage);

        return new UserResponseDTO(newUser.getId(), newUser.getUsername(),  newUser.getEmail());
    }

    public String verifyEmail(String emailVerificationToken){
        User userToVerify = userRepository.findByEmailVerificationToken(emailVerificationToken);

        if (userToVerify == null) {
            throw new BadCredentialsException("Invalid verification token");
        }

        userToVerify.setVerified(true);
        userToVerify.setEmailVerificationToken(null);
        userRepository.save(userToVerify);
        return "Success";
    }

    public String forgotPassword(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new BadCredentialsException("User not found");
        }

        String resetToken = UUID.randomUUID().toString();
        user.setResetPasswordToken(resetToken);
        userRepository.save(user);

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Reset your password");
        mailMessage.setText("Click the link to reset your password: http://localhost:8080/api/auth/reset-password?token=" + resetToken);
        javaMailSender.send(mailMessage);

        return "Reset link sent to your email";
    }

    public String resetPassword(String token, String newPassword) {
        User user = userRepository.findByResetPasswordToken(token);
        if (user == null) {
            throw new BadCredentialsException("Invalid reset token");
        }

        String hashedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(hashedPassword);
        user.setResetPasswordToken(null);
        userRepository.save(user);

        return "Password reset successful";
    }
}
