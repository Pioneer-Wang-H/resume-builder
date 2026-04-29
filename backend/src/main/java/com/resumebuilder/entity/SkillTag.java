package com.resumebuilder.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("skill_tag")
public class SkillTag {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private String skillName;
    private Integer proficiency;
    private String category;
    private Integer sortOrder;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
