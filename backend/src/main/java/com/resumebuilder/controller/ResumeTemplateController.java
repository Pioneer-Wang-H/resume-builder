package com.resumebuilder.controller;

import com.resumebuilder.common.Result;
import com.resumebuilder.entity.ResumeTemplate;
import com.resumebuilder.service.ResumeTemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/template")
@RequiredArgsConstructor
public class ResumeTemplateController {

    private final ResumeTemplateService service;

    @GetMapping
    public Result<List<ResumeTemplate>> list() {
        return Result.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public Result<ResumeTemplate> get(@PathVariable Long id) {
        return Result.ok(service.getById(id));
    }
}
