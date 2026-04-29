package com.resumebuilder.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("resume_template")
public class ResumeTemplate {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private String thumbnail;
    private String templateFile;
    private Integer isBuiltin;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
