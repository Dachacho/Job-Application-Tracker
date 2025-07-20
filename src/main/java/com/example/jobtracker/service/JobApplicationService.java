package com.example.jobtracker.service;

import com.example.jobtracker.dto.JobApplicationDTO;
import com.example.jobtracker.mapper.JobApplicationMapper;
import com.example.jobtracker.model.JobApplication;
import com.example.jobtracker.model.User;
import com.example.jobtracker.repository.JobApplicationRepository;
import com.example.jobtracker.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JobApplicationService {
    private final JobApplicationRepository jobApplicationRepository;
    private final UserRepository userRepository;

    public JobApplicationService(JobApplicationRepository jobApplicationRepository, UserRepository userRepository) {
        this.jobApplicationRepository = jobApplicationRepository;
        this.userRepository = userRepository;
    }

    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username);
    }

    public List<JobApplicationDTO> getAllApplications() {
        User currentUser = getCurrentUser();
        return jobApplicationRepository.findByUser(currentUser).stream()
                .map(JobApplicationMapper::toDTO)
                .toList();
    }

    public Optional<JobApplicationDTO> getApplicationById(Long id) {
        User currentUser = getCurrentUser();
        return jobApplicationRepository.findByIdAndUser(id, currentUser)
                .map(JobApplicationMapper::toDTO);
    }

    public JobApplicationDTO createApplication(JobApplicationDTO jobApplicationDTO) {
        User currentUser = getCurrentUser();
        JobApplication entity = JobApplicationMapper.toEntity(jobApplicationDTO);
        entity.setUser(currentUser);
        JobApplication saved = jobApplicationRepository.save(entity);
        return JobApplicationMapper.toDTO(saved);
    }

    public JobApplicationDTO updateApplication(Long id, JobApplicationDTO jobApplicationDTO) {
        User currentUser = getCurrentUser();
        return jobApplicationRepository.findByIdAndUser(id, currentUser)
                .map(existingApplication -> {
                   existingApplication.setCompany(jobApplicationDTO.getCompany());
                   existingApplication.setPosition(jobApplicationDTO.getPosition());
                   existingApplication.setStatus(jobApplicationDTO.getStatus());
                   existingApplication.setAppliedDate(jobApplicationDTO.getAppliedDate());
                   JobApplication updatedApplication = jobApplicationRepository.save(existingApplication);
                   return JobApplicationMapper.toDTO(updatedApplication);
                }).orElseThrow(() -> new EntityNotFoundException("Job Application not found"));
    }

    public void deleteApplication(Long id) {
        User currentUser = getCurrentUser();
        jobApplicationRepository.findByIdAndUser(id, currentUser)
            .ifPresent(jobApplicationRepository::delete);
    }
}
