package com.resumebuilder.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.resumebuilder.entity.SelfEvaluation;
import com.resumebuilder.mapper.SelfEvaluationMapper;
import com.resumebuilder.service.SelfEvaluationService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SelfEvaluationServiceImpl implements SelfEvaluationService {

    private final SelfEvaluationMapper mapper;

    @Override
    public SelfEvaluation getByUserId(Long userId) {
        return mapper.selectOne(new LambdaQueryWrapper<SelfEvaluation>()
                .eq(SelfEvaluation::getUserId, userId));
    }

    @Override
    @CacheEvict(value = "resumeAssemble", allEntries = true)
    public void saveOrUpdate(SelfEvaluation evaluation) {
        SelfEvaluation exist = mapper.selectOne(new LambdaQueryWrapper<SelfEvaluation>()
                .eq(SelfEvaluation::getUserId, evaluation.getUserId()));
        if (exist != null) {
            evaluation.setId(exist.getId());
            mapper.updateById(evaluation);
        } else {
            mapper.insert(evaluation);
        }
    }
}
