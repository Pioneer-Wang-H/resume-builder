package com.resumebuilder.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.resumebuilder.entity.Certificate;
import com.resumebuilder.mapper.CertificateMapper;
import com.resumebuilder.service.CertificateService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CertificateServiceImpl implements CertificateService {

    private final CertificateMapper mapper;

    @Override
    public List<Certificate> getByUserId(Long userId) {
        return mapper.selectList(new LambdaQueryWrapper<Certificate>()
                .eq(Certificate::getUserId, userId)
                .orderByAsc(Certificate::getSortOrder));
    }

    @Override
    @CacheEvict(value = "resumeAssemble", allEntries = true)
    public void save(Certificate certificate) {
        mapper.insert(certificate);
    }

    @Override
    @CacheEvict(value = "resumeAssemble", allEntries = true)
    public void update(Certificate certificate) {
        mapper.updateById(certificate);
    }

    @Override
    @CacheEvict(value = "resumeAssemble", allEntries = true)
    public void delete(Long id) {
        mapper.deleteById(id);
    }
}
