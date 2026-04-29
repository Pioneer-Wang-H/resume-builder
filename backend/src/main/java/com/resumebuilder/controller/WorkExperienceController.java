package com.resumebuilder.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.resumebuilder.annotation.OperationLog;
import com.resumebuilder.common.Result;
import com.resumebuilder.entity.WorkExperience;
import com.resumebuilder.service.WorkExperienceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/work-experience")
@RequiredArgsConstructor
public class WorkExperienceController {

    private final WorkExperienceService service;

    @GetMapping
    public Result<List<WorkExperience>> list() {
        return Result.ok(service.getByUserId(StpUtil.getLoginIdAsLong()));
    }

    @GetMapping("/{id}")
    public Result<WorkExperience> get(@PathVariable Long id) {
        return Result.ok(service.getById(id));
    }

    @PostMapping
    @OperationLog(module = "素材管理", operation = "新增工作经历")
    public Result<?> save(@RequestBody WorkExperience experience) {
        experience.setUserId(StpUtil.getLoginIdAsLong());
        service.save(experience);
        return Result.ok();
    }

    @PutMapping("/{id}")
    @OperationLog(module = "素材管理", operation = "更新工作经历")
    public Result<?> update(@PathVariable Long id, @RequestBody WorkExperience experience) {
        experience.setId(id);
        experience.setUserId(StpUtil.getLoginIdAsLong());
        service.update(experience);
        return Result.ok();
    }

    @DeleteMapping("/{id}")
    @OperationLog(module = "素材管理", operation = "删除工作经历")
    public Result<?> delete(@PathVariable Long id) {
        service.delete(id);
        return Result.ok();
    }
}
