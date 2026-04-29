package com.resumebuilder.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("project_experience")
public class ProjectExperience {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private String projectName;
    private String role;
    private LocalDate startDate;
    private LocalDate endDate;
    private String url;
    private String description;
    private String highlights;
    private Integer sortOrder;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
