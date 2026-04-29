package com.resumebuilder.service.impl;

import com.resumebuilder.entity.ResumeTemplate;
import com.resumebuilder.mapper.ResumeTemplateMapper;
import com.resumebuilder.service.ResumeTemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ResumeTemplateServiceImpl implements ResumeTemplateService {

    private final ResumeTemplateMapper mapper;

    @Override
    @Cacheable(value = "resumeTemplate", key = "'all'")
    public List<ResumeTemplate> getAll() {
        return mapper.selectList(null);
    }

    @Override
    @Cacheable(value = "resumeTemplate", key = "#id")
    public ResumeTemplate getById(Long id) {
        return mapper.selectById(id);
    }
}
