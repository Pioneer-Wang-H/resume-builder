package com.resumebuilder.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.resumebuilder.annotation.OperationLog;
import com.resumebuilder.common.Result;
import com.resumebuilder.entity.Education;
import com.resumebuilder.service.EducationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/education")
@RequiredArgsConstructor
public class EducationController {

    private final EducationService educationService;

    @GetMapping
    public Result<List<Education>> list() {
        return Result.ok(educationService.getByUserId(StpUtil.getLoginIdAsLong()));
    }

    @GetMapping("/{id}")
    public Result<Education> get(@PathVariable Long id) {
        return Result.ok(educationService.getById(id));
    }

    @PostMapping
    @OperationLog(module = "素材管理", operation = "新增教育经历")
    public Result<?> save(@RequestBody Education education) {
        education.setUserId(StpUtil.getLoginIdAsLong());
        educationService.save(education);
        return Result.ok();
    }

    @PutMapping("/{id}")
    @OperationLog(module = "素材管理", operation = "更新教育经历")
    public Result<?> update(@PathVariable Long id, @RequestBody Education education) {
        education.setId(id);
        education.setUserId(StpUtil.getLoginIdAsLong());
        educationService.update(education);
        return Result.ok();
    }

    @DeleteMapping("/{id}")
    @OperationLog(module = "素材管理", operation = "删除教育经历")
    public Result<?> delete(@PathVariable Long id) {
        educationService.delete(id);
        return Result.ok();
    }
}
