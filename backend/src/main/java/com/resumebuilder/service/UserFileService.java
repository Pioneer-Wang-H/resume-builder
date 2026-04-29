package com.resumebuilder.service;

import com.resumebuilder.entity.UserFile;

public interface UserFileService {
    UserFile save(Long userId, String fileName, String filePath, String fileType, Long fileSize);
}
