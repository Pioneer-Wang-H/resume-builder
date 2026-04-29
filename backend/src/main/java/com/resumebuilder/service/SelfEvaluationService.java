package com.resumebuilder.service;

import com.resumebuilder.entity.SelfEvaluation;

public interface SelfEvaluationService {
    SelfEvaluation getByUserId(Long userId);
    void saveOrUpdate(SelfEvaluation evaluation);
}
