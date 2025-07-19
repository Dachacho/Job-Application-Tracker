package com.example.jobtracker.controller;

import com.example.jobtracker.dto.JobApplicationDTO;
import com.example.jobtracker.service.JobApplicationService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/job-applications")
public class JobApplicationController {
    private final JobApplicationService jobApplicationService;

    public JobApplicationController(JobApplicationService jobApplicationService) {
        this.jobApplicationService = jobApplicationService;
    }

    //endpoints
    @GetMapping
    public ResponseEntity<List<JobApplicationDTO>> getAllApplications() {
        return ResponseEntity.ok(jobApplicationService.getAllApplications());
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobApplicationDTO> getApplicationById(@PathVariable Long id) {
        return jobApplicationService.getApplicationById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<JobApplicationDTO> createApplication(@RequestBody JobApplicationDTO jobApplicationDTO) {
        JobApplicationDTO createdApplication = jobApplicationService.createApplication(jobApplicationDTO);
        return ResponseEntity.ok(createdApplication);
    }

    @PutMapping("/{id}")
    public ResponseEntity<JobApplicationDTO> updateApplication(
            @PathVariable Long id, @RequestBody JobApplicationDTO jobApplicationDTO) {
        try {
            JobApplicationDTO updatedApplication = jobApplicationService.updateApplication(id, jobApplicationDTO);
            return ResponseEntity.ok(updatedApplication);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteApplication(@PathVariable Long id) {
        jobApplicationService.deleteApplication(id);
        return ResponseEntity.noContent().build();
    }
}
