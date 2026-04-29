package com.resumebuilder.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("user_file")
public class UserFile {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private String fileName;
    private String filePath;
    private String fileType;
    private Long fileSize;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
