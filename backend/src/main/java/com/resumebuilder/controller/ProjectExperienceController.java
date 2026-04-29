package com.resumebuilder.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.resumebuilder.annotation.OperationLog;
import com.resumebuilder.common.Result;
import com.resumebuilder.entity.ProjectExperience;
import com.resumebuilder.service.ProjectExperienceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/project-experience")
@RequiredArgsConstructor
public class ProjectExperienceController {

    private final ProjectExperienceService service;

    @GetMapping
    public Result<List<ProjectExperience>> list() {
        return Result.ok(service.getByUserId(StpUtil.getLoginIdAsLong()));
    }

    @GetMapping("/{id}")
    public Result<ProjectExperience> get(@PathVariable Long id) {
        return Result.ok(service.getById(id));
    }

    @PostMapping
    @OperationLog(module = "素材管理", operation = "新增项目经历")
    public Result<?> save(@RequestBody ProjectExperience experience) {
        experience.setUserId(StpUtil.getLoginIdAsLong());
        service.save(experience);
        return Result.ok();
    }

    @PutMapping("/{id}")
    @OperationLog(module = "素材管理", operation = "更新项目经历")
    public Result<?> update(@PathVariable Long id, @RequestBody ProjectExperience experience) {
        experience.setId(id);
        experience.setUserId(StpUtil.getLoginIdAsLong());
        service.update(experience);
        return Result.ok();
    }

    @DeleteMapping("/{id}")
    @OperationLog(module = "素材管理", operation = "删除项目经历")
    public Result<?> delete(@PathVariable Long id) {
        service.delete(id);
        return Result.ok();
    }
}
