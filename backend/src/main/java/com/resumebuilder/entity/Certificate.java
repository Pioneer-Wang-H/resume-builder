package com.resumebuilder.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("certificate")
public class Certificate {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private String name;
    private String issuingAuthority;
    private LocalDate obtainDate;
    private String description;
    private Integer sortOrder;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
