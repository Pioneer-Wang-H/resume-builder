package com.resumebuilder.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.resumebuilder.annotation.OperationLog;
import com.resumebuilder.common.Result;
import com.resumebuilder.entity.SelfEvaluation;
import com.resumebuilder.service.SelfEvaluationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/self-evaluation")
@RequiredArgsConstructor
public class SelfEvaluationController {

    private final SelfEvaluationService service;

    @GetMapping
    public Result<SelfEvaluation> get() {
        return Result.ok(service.getByUserId(StpUtil.getLoginIdAsLong()));
    }

    @PutMapping
    @OperationLog(module = "素材管理", operation = "更新自我评价")
    public Result<?> save(@RequestBody SelfEvaluation evaluation) {
        evaluation.setUserId(StpUtil.getLoginIdAsLong());
        service.saveOrUpdate(evaluation);
        return Result.ok();
    }
}
