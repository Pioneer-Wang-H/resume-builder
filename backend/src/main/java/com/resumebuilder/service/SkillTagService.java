package com.resumebuilder.service;

import com.resumebuilder.entity.SkillTag;

import java.util.List;

public interface SkillTagService {
    List<SkillTag> getByUserId(Long userId);
    void save(SkillTag skillTag);
    void update(SkillTag skillTag);
    void delete(Long id);
    void batchSave(List<SkillTag> skillTags);
}
