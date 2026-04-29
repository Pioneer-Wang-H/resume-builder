package com.resumebuilder.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.resumebuilder.entity.BasicInfo;
import com.resumebuilder.mapper.BasicInfoMapper;
import com.resumebuilder.service.BasicInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicInfoServiceImpl implements BasicInfoService {

    private final BasicInfoMapper basicInfoMapper;

    @Override
    public BasicInfo getByUserId(Long userId) {
        return basicInfoMapper.selectOne(new LambdaQueryWrapper<BasicInfo>()
                .eq(BasicInfo::getUserId, userId));
    }

    @Override
    @CacheEvict(value = "resumeAssemble", allEntries = true)
    public void saveOrUpdate(BasicInfo info) {
        BasicInfo exist = basicInfoMapper.selectOne(new LambdaQueryWrapper<BasicInfo>()
                .eq(BasicInfo::getUserId, info.getUserId()));
        if (exist != null) {
            info.setId(exist.getId());
            basicInfoMapper.updateById(info);
        } else {
            basicInfoMapper.insert(info);
        }
    }
}
