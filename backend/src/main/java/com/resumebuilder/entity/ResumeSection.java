package com.resumebuilder.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

@Data
@TableName("resume_section")
public class ResumeSection {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long resumeId;
    private String sectionType;
    private Integer enabled;
    private Integer sortOrder;
}
