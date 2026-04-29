package com.resumebuilder.service;

import com.resumebuilder.entity.ResumeTemplate;

import java.util.List;

public interface ResumeTemplateService {
    List<ResumeTemplate> getAll();
    ResumeTemplate getById(Long id);
}
