package com.resumebuilder.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.resumebuilder.entity.WorkExperience;
import com.resumebuilder.mapper.WorkExperienceMapper;
import com.resumebuilder.service.WorkExperienceService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkExperienceServiceImpl implements WorkExperienceService {

    private final WorkExperienceMapper mapper;

    @Override
    public List<WorkExperience> getByUserId(Long userId) {
        return mapper.selectList(new LambdaQueryWrapper<WorkExperience>()
                .eq(WorkExperience::getUserId, userId)
                .orderByAsc(WorkExperience::getSortOrder));
    }

    @Override
    public WorkExperience getById(Long id) {
        return mapper.selectById(id);
    }

    @Override
    @CacheEvict(value = "resumeAssemble", allEntries = true)
    public void save(WorkExperience experience) {
        mapper.insert(experience);
    }

    @Override
    @CacheEvict(value = "resumeAssemble", allEntries = true)
    public void update(WorkExperience experience) {
        mapper.updateById(experience);
    }

    @Override
    @CacheEvict(value = "resumeAssemble", allEntries = true)
    public void delete(Long id) {
        mapper.deleteById(id);
    }
}
