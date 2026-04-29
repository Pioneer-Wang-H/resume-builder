package com.resumebuilder.service.impl;

import com.resumebuilder.entity.UserFile;
import com.resumebuilder.mapper.UserFileMapper;
import com.resumebuilder.service.UserFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserFileServiceImpl implements UserFileService {

    private final UserFileMapper userFileMapper;

    @Override
    public UserFile save(Long userId, String fileName, String filePath, String fileType, Long fileSize) {
        UserFile uf = new UserFile();
        uf.setUserId(userId);
        uf.setFileName(fileName);
        uf.setFilePath(filePath);
        uf.setFileType(fileType);
        uf.setFileSize(fileSize);
        userFileMapper.insert(uf);
        return uf;
    }
}
