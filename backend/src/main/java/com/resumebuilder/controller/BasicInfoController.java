package com.resumebuilder.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.resumebuilder.annotation.OperationLog;
import com.resumebuilder.common.Result;
import com.resumebuilder.entity.BasicInfo;
import com.resumebuilder.service.BasicInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/basic-info")
@RequiredArgsConstructor
public class BasicInfoController {

    private final BasicInfoService basicInfoService;

    @GetMapping
    public Result<BasicInfo> get() {
        return Result.ok(basicInfoService.getByUserId(StpUtil.getLoginIdAsLong()));
    }

    @PutMapping
    @OperationLog(module = "素材管理", operation = "更新基本信息")
    public Result<?> save(@RequestBody BasicInfo basicInfo) {
        basicInfo.setUserId(StpUtil.getLoginIdAsLong());
        basicInfoService.saveOrUpdate(basicInfo);
        return Result.ok();
    }
}
