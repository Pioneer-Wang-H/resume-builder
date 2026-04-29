package com.resumebuilder.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.resumebuilder.entity.Education;
import com.resumebuilder.mapper.EducationMapper;
import com.resumebuilder.service.EducationService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EducationServiceImpl implements EducationService {

    private final EducationMapper educationMapper;

    @Override
    public List<Education> getByUserId(Long userId) {
        return educationMapper.selectList(new LambdaQueryWrapper<Education>()
                .eq(Education::getUserId, userId)
                .orderByAsc(Education::getSortOrder));
    }

    @Override
    public Education getById(Long id) {
        return educationMapper.selectById(id);
    }

    @Override
    @CacheEvict(value = "resumeAssemble", allEntries = true)
    public void save(Education education) {
        educationMapper.insert(education);
    }

    @Override
    @CacheEvict(value = "resumeAssemble", allEntries = true)
    public void update(Education education) {
        educationMapper.updateById(education);
    }

    @Override
    @CacheEvict(value = "resumeAssemble", allEntries = true)
    public void delete(Long id) {
        educationMapper.deleteById(id);
    }
}
