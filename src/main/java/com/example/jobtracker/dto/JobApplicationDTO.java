package com.example.jobtracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobApplicationDTO {
    private Long id;
    private String company;
    private String position;
    private String status;
    private LocalDate appliedDate;
    private Long userId;
}
