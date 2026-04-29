package com.resumebuilder.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.resumebuilder.common.Result;
import com.resumebuilder.dto.ParsedResumeData;
import com.resumebuilder.service.ResumeParserService;
import com.resumebuilder.service.UserFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
@RequestMapping("/api/file")
@RequiredArgsConstructor
public class FileUploadController {

    private final ResumeParserService resumeParserService;
    private final UserFileService userFileService;

    private static final Path UPLOAD_DIR = Paths.get("uploads", "resume_imports").toAbsolutePath().normalize();

    @PostMapping("/upload-resume")
    public Result<ParsedResumeData> uploadResume(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) return Result.error(400, "文件为空");

        String originalName = file.getOriginalFilename();
        String ext = originalName != null && originalName.contains(".")
                ? originalName.substring(originalName.lastIndexOf(".")) : "";
        String storedName = UUID.randomUUID() + ext;

        try {
            Files.createDirectories(UPLOAD_DIR);
            Path targetPath = UPLOAD_DIR.resolve(storedName);
            file.transferTo(targetPath.toFile());

            userFileService.save(StpUtil.getLoginIdAsLong(), originalName,
                    targetPath.toString(), ext, file.getSize());

            ParsedResumeData parsed = resumeParserService.parse(targetPath.toFile());
            return Result.ok(parsed);
        } catch (IOException e) {
            return Result.error(500, "文件保存失败: " + e.getMessage());
        }
    }

    @PostMapping("/import-resume")
    public Result<?> importResume(@RequestBody ParsedResumeData data) {
        resumeParserService.importData(StpUtil.getLoginIdAsLong(), data);
        return Result.ok();
    }
}
