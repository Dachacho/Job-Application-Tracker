package com.example.jobtracker.mapper;

import com.example.jobtracker.dto.JobApplicationDTO;
import com.example.jobtracker.model.JobApplication;

public class JobApplicationMapper {
    public static JobApplicationDTO toDTO(JobApplication entity) {
        if (entity == null) {
            return null;
        }
        JobApplicationDTO dto = new JobApplicationDTO();
        dto.setId(entity.getId());
        dto.setCompany(entity.getCompany());
        dto.setPosition(entity.getPosition());
        dto.setStatus(entity.getStatus());
        dto.setAppliedDate(entity.getAppliedDate());
        return dto;
    }

    public static JobApplication toEntity(JobApplicationDTO dto) {
        if (dto == null) {
            return null;
        }
        JobApplication entity = new JobApplication();
        entity.setId(dto.getId());
        entity.setCompany(dto.getCompany());
        entity.setPosition(dto.getPosition());
        entity.setStatus(dto.getStatus());
        entity.setAppliedDate(dto.getAppliedDate());
        return entity;
    }
}
