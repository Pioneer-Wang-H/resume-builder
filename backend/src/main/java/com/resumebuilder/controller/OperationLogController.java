package com.resumebuilder.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.resumebuilder.common.Result;
import com.resumebuilder.entity.OperationLog;
import com.resumebuilder.mapper.OperationLogMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/operation-log")
@RequiredArgsConstructor
public class OperationLogController {

    private final OperationLogMapper operationLogMapper;

    @GetMapping
    public Result<Page<OperationLog>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int pageSize,
            @RequestParam(required = false) String module) {
        LambdaQueryWrapper<OperationLog> qw = new LambdaQueryWrapper<>();
        if (module != null && !module.isEmpty()) {
            qw.eq(OperationLog::getModule, module);
        }
        qw.orderByDesc(OperationLog::getCreateTime);
        return Result.ok(operationLogMapper.selectPage(new Page<>(page, pageSize), qw));
    }
}
