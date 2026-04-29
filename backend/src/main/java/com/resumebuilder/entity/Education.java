package com.resumebuilder.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("education")
public class Education {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private String school;
    private String major;
    private String degree;
    private String gpa;
    private LocalDate startDate;
    private LocalDate endDate;
    private String description;
    private Integer sortOrder;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
