package com.resumebuilder.service;

import com.resumebuilder.entity.Education;

import java.util.List;

public interface EducationService {
    List<Education> getByUserId(Long userId);
    Education getById(Long id);
    void save(Education education);
    void update(Education education);
    void delete(Long id);
}
