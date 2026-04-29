package com.resumebuilder.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

@Data
@TableName("resume_section_item")
public class ResumeSectionItem {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long sectionId;
    private String itemType;
    private Long itemId;
    private Integer sortOrder;
}
