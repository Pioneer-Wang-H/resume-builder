package com.resumebuilder.service;

import com.resumebuilder.entity.BasicInfo;

public interface BasicInfoService {
    BasicInfo getByUserId(Long userId);
    void saveOrUpdate(BasicInfo basicInfo);
}
