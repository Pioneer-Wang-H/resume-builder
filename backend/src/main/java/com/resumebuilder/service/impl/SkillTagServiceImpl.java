package com.resumebuilder.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.resumebuilder.entity.SkillTag;
import com.resumebuilder.mapper.SkillTagMapper;
import com.resumebuilder.service.SkillTagService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SkillTagServiceImpl implements SkillTagService {

    private final SkillTagMapper mapper;

    @Override
    public List<SkillTag> getByUserId(Long userId) {
        return mapper.selectList(new LambdaQueryWrapper<SkillTag>()
                .eq(SkillTag::getUserId, userId)
                .orderByAsc(SkillTag::getSortOrder));
    }

    @Override
    @CacheEvict(value = "resumeAssemble", allEntries = true)
    public void save(SkillTag skillTag) {
        mapper.insert(skillTag);
    }

    @Override
    @CacheEvict(value = "resumeAssemble", allEntries = true)
    public void update(SkillTag skillTag) {
        mapper.updateById(skillTag);
    }

    @Override
    @CacheEvict(value = "resumeAssemble", allEntries = true)
    public void delete(Long id) {
        mapper.deleteById(id);
    }

    @Override
    @CacheEvict(value = "resumeAssemble", allEntries = true)
    public void batchSave(List<SkillTag> skillTags) {
        skillTags.forEach(mapper::insert);
    }
}
