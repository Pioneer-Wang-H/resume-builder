package com.resumebuilder.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ResumeCreateDTO {
    @NotBlank(message = "简历标题不能为空")
    private String title;
    private Long templateId;
}
