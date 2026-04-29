package com.resumebuilder.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.resumebuilder.annotation.OperationLog;
import com.resumebuilder.common.Result;
import com.resumebuilder.dto.ResumeCreateDTO;
import com.resumebuilder.dto.ResumeSectionConfigDTO;
import com.resumebuilder.entity.Resume;
import com.resumebuilder.service.ResumeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resume")
@RequiredArgsConstructor
public class ResumeController {

    private final ResumeService resumeService;

    @GetMapping
    public Result<List<Resume>> list() {
        return Result.ok(resumeService.getByUserId(StpUtil.getLoginIdAsLong()));
    }

    @GetMapping("/{id}")
    public Result<Resume> get(@PathVariable Long id) {
        return Result.ok(resumeService.getById(id));
    }

    @PostMapping
    @OperationLog(module = "简历管理", operation = "创建简历")
    public Result<Resume> create(@Valid @RequestBody ResumeCreateDTO dto) {
        return Result.ok(resumeService.create(StpUtil.getLoginIdAsLong(), dto));
    }

    @DeleteMapping("/{id}")
    @OperationLog(module = "简历管理", operation = "删除简历")
    public Result<?> delete(@PathVariable Long id) {
        resumeService.delete(id);
        return Result.ok();
    }

    @PutMapping("/{id}/title")
    @OperationLog(module = "简历管理", operation = "修改简历标题")
    public Result<?> updateTitle(@PathVariable Long id, @RequestBody ResumeCreateDTO dto) {
        resumeService.updateTitle(id, dto.getTitle());
        return Result.ok();
    }

    @PutMapping("/{id}/default")
    @OperationLog(module = "简历管理", operation = "设置默认简历")
    public Result<?> setDefault(@PathVariable Long id) {
        resumeService.setDefault(StpUtil.getLoginIdAsLong(), id);
        return Result.ok();
    }

    @PutMapping("/{id}/sections")
    @OperationLog(module = "简历管理", operation = "配置简历模块")
    public Result<?> configureSections(@PathVariable Long id, @RequestBody List<ResumeSectionConfigDTO> sections) {
        resumeService.configureSections(id, sections);
        return Result.ok();
    }

    @GetMapping("/{id}/assemble")
    public Result<?> assemble(@PathVariable Long id) {
        return Result.ok(resumeService.assemble(id));
    }

    @GetMapping("/{id}/export-pdf")
    public ResponseEntity<byte[]> exportPdf(@PathVariable Long id) {
        byte[] pdfBytes = resumeService.exportPdf(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "resume-" + id + ".pdf");
        return ResponseEntity.ok().headers(headers).body(pdfBytes);
    }
}
