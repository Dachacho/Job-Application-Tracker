package com.example.jobtracker.service;

import com.example.jobtracker.dto.JobApplicationDTO;
import com.example.jobtracker.mapper.JobApplicationMapper;
import com.example.jobtracker.model.JobApplication;
import com.example.jobtracker.repository.JobApplicationRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JobApplicationService {
    private final JobApplicationRepository jobApplicationRepository;

    public JobApplicationService(JobApplicationRepository jobApplicationRepository) {
        this.jobApplicationRepository = jobApplicationRepository;
    }

    public List<JobApplicationDTO> getAllApplications() {
        return jobApplicationRepository.findAll().stream()
                .map(JobApplicationMapper::toDTO)
                .toList();
    }

    public Optional<JobApplicationDTO> getApplicationById(Long id) {
        return jobApplicationRepository.findById(id).stream()
                .findFirst().map(JobApplicationMapper::toDTO);
    }

    public JobApplicationDTO createApplication(JobApplicationDTO jobApplicationDTO) {
        JobApplication entity = JobApplicationMapper.toEntity(jobApplicationDTO);
        JobApplication saved = jobApplicationRepository.save(entity);
        return JobApplicationMapper.toDTO(saved);
    }

    public JobApplicationDTO updateApplication(Long id, JobApplicationDTO jobApplicationDTO) {
        return jobApplicationRepository.findById(id)
                .map(existingApplication -> {
                   existingApplication.setCompany(jobApplicationDTO.getCompany());
                   existingApplication.setPosition(jobApplicationDTO.getPosition());
                   existingApplication.setStatus(jobApplicationDTO.getStatus());
                   existingApplication.setAppliedDate(jobApplicationDTO.getAppliedDate());
                   JobApplication updatedApplication = jobApplicationRepository.save(existingApplication);
                   return JobApplicationMapper.toDTO(updatedApplication);
                }).orElseThrow(() -> new EntityNotFoundException("Job Application now found"));
    }

    public void deleteApplication(Long id) {
        jobApplicationRepository.deleteById(id);
    }
}
