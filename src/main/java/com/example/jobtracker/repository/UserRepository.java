package com.example.jobtracker.repository;

import com.example.jobtracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    // Custom query methods can be defined here if needed
    User findByUsername(String username);
    User findByEmailVerificationToken(String emailVerificationToken);
    User findByEmail(String email);
    User findByResetPasswordToken(String resetPasswordToken);
}
