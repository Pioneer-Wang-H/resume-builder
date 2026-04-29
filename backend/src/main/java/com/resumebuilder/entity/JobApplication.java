package com.resumebuilder.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("job_application")
public class JobApplication {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private Long resumeId;
    private String company;
    private String position;
    private String salaryRange;
    private String location;
    private String channel;
    private String status;
    private LocalDate applyDate;
    private String notes;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
