package com.example.jobtracker.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class JobApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String company;
    private String position;
    private String status;
    private LocalDate appliedDate;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
