package com.resumebuilder.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.resumebuilder.annotation.OperationLog;
import com.resumebuilder.common.Result;
import com.resumebuilder.entity.Certificate;
import com.resumebuilder.service.CertificateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/certificate")
@RequiredArgsConstructor
public class CertificateController {

    private final CertificateService service;

    @GetMapping
    public Result<List<Certificate>> list() {
        return Result.ok(service.getByUserId(StpUtil.getLoginIdAsLong()));
    }

    @PostMapping
    @OperationLog(module = "素材管理", operation = "新增证书奖项")
    public Result<?> save(@RequestBody Certificate certificate) {
        certificate.setUserId(StpUtil.getLoginIdAsLong());
        service.save(certificate);
        return Result.ok();
    }

    @PutMapping("/{id}")
    @OperationLog(module = "素材管理", operation = "更新证书奖项")
    public Result<?> update(@PathVariable Long id, @RequestBody Certificate certificate) {
        certificate.setId(id);
        service.update(certificate);
        return Result.ok();
    }

    @DeleteMapping("/{id}")
    @OperationLog(module = "素材管理", operation = "删除证书奖项")
    public Result<?> delete(@PathVariable Long id) {
        service.delete(id);
        return Result.ok();
    }
}
