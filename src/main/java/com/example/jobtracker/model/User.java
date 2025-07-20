package com.example.jobtracker.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue
    private Long id;
    private String email;
    private String username;
    private String password;
    private boolean isVerified;
    private String emailVerificationToken;
    private String resetPasswordToken;
}
