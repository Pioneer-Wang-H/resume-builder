package com.resumebuilder.service;

import com.resumebuilder.entity.JobApplication;

import java.util.List;
import java.util.Map;

public interface JobApplicationService {
    List<JobApplication> getByUserId(Long userId);
    void save(JobApplication application);
    void update(JobApplication application);
    void delete(Long id);
    Map<String, Object> getStats(Long userId);
}
