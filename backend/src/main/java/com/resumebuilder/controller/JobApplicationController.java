package com.resumebuilder.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.resumebuilder.annotation.OperationLog;
import com.resumebuilder.common.Result;
import com.resumebuilder.entity.JobApplication;
import com.resumebuilder.service.JobApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/job-application")
@RequiredArgsConstructor
public class JobApplicationController {

    private final JobApplicationService service;

    @GetMapping
    public Result<List<JobApplication>> list() {
        return Result.ok(service.getByUserId(StpUtil.getLoginIdAsLong()));
    }

    @GetMapping("/stats")
    public Result<?> stats() {
        return Result.ok(service.getStats(StpUtil.getLoginIdAsLong()));
    }

    @PostMapping
    @OperationLog(module = "投递管理", operation = "新增投递记录")
    public Result<?> save(@RequestBody JobApplication application) {
        application.setUserId(StpUtil.getLoginIdAsLong());
        service.save(application);
        return Result.ok();
    }

    @PutMapping("/{id}")
    @OperationLog(module = "投递管理", operation = "更新投递记录")
    public Result<?> update(@PathVariable Long id, @RequestBody JobApplication application) {
        application.setId(id);
        service.update(application);
        return Result.ok();
    }

    @DeleteMapping("/{id}")
    @OperationLog(module = "投递管理", operation = "删除投递记录")
    public Result<?> delete(@PathVariable Long id) {
        service.delete(id);
        return Result.ok();
    }
}
