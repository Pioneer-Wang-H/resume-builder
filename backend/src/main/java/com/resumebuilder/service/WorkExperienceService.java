package com.resumebuilder.service;

import com.resumebuilder.entity.WorkExperience;

import java.util.List;

public interface WorkExperienceService {
    List<WorkExperience> getByUserId(Long userId);
    WorkExperience getById(Long id);
    void save(WorkExperience experience);
    void update(WorkExperience experience);
    void delete(Long id);
}
