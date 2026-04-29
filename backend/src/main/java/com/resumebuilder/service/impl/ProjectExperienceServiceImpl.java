package com.resumebuilder.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.resumebuilder.entity.ProjectExperience;
import com.resumebuilder.mapper.ProjectExperienceMapper;
import com.resumebuilder.service.ProjectExperienceService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectExperienceServiceImpl implements ProjectExperienceService {

    private final ProjectExperienceMapper mapper;

    @Override
    public List<ProjectExperience> getByUserId(Long userId) {
        return mapper.selectList(new LambdaQueryWrapper<ProjectExperience>()
                .eq(ProjectExperience::getUserId, userId)
                .orderByAsc(ProjectExperience::getSortOrder));
    }

    @Override
    public ProjectExperience getById(Long id) {
        return mapper.selectById(id);
    }

    @Override
    @CacheEvict(value = "resumeAssemble", allEntries = true)
    public void save(ProjectExperience experience) {
        mapper.insert(experience);
    }

    @Override
    @CacheEvict(value = "resumeAssemble", allEntries = true)
    public void update(ProjectExperience experience) {
        mapper.updateById(experience);
    }

    @Override
    @CacheEvict(value = "resumeAssemble", allEntries = true)
    public void delete(Long id) {
        mapper.deleteById(id);
    }
}
