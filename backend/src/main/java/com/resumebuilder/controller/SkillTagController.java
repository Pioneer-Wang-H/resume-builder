package com.resumebuilder.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.resumebuilder.annotation.OperationLog;
import com.resumebuilder.common.Result;
import com.resumebuilder.entity.SkillTag;
import com.resumebuilder.service.SkillTagService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/skill-tag")
@RequiredArgsConstructor
public class SkillTagController {

    private final SkillTagService service;

    @GetMapping
    public Result<List<SkillTag>> list() {
        return Result.ok(service.getByUserId(StpUtil.getLoginIdAsLong()));
    }

    @PostMapping
    @OperationLog(module = "素材管理", operation = "新增技能标签")
    public Result<?> save(@RequestBody SkillTag skillTag) {
        skillTag.setUserId(StpUtil.getLoginIdAsLong());
        service.save(skillTag);
        return Result.ok();
    }

    @PostMapping("/batch")
    @OperationLog(module = "素材管理", operation = "批量保存技能标签")
    public Result<?> batchSave(@RequestBody List<SkillTag> skillTags) {
        skillTags.forEach(tag -> tag.setUserId(StpUtil.getLoginIdAsLong()));
        service.batchSave(skillTags);
        return Result.ok();
    }

    @PutMapping("/{id}")
    @OperationLog(module = "素材管理", operation = "更新技能标签")
    public Result<?> update(@PathVariable Long id, @RequestBody SkillTag skillTag) {
        skillTag.setId(id);
        service.update(skillTag);
        return Result.ok();
    }

    @DeleteMapping("/{id}")
    @OperationLog(module = "素材管理", operation = "删除技能标签")
    public Result<?> delete(@PathVariable Long id) {
        service.delete(id);
        return Result.ok();
    }
}
