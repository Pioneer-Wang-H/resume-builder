package com.resumebuilder.service;

import com.resumebuilder.entity.ProjectExperience;

import java.util.List;

public interface ProjectExperienceService {
    List<ProjectExperience> getByUserId(Long userId);
    ProjectExperience getById(Long id);
    void save(ProjectExperience experience);
    void update(ProjectExperience experience);
    void delete(Long id);
}
