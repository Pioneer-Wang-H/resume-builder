package com.resumebuilder.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.resumebuilder.entity.JobApplication;
import com.resumebuilder.mapper.JobApplicationMapper;
import com.resumebuilder.service.JobApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class JobApplicationServiceImpl implements JobApplicationService {

    private final JobApplicationMapper mapper;

    @Override
    public List<JobApplication> getByUserId(Long userId) {
        return mapper.selectList(new LambdaQueryWrapper<JobApplication>()
                .eq(JobApplication::getUserId, userId)
                .orderByDesc(JobApplication::getCreateTime));
    }

    @Override
    public void save(JobApplication application) {
        mapper.insert(application);
    }

    @Override
    public void update(JobApplication application) {
        mapper.updateById(application);
    }

    @Override
    public void delete(Long id) {
        mapper.deleteById(id);
    }

    @Override
    public Map<String, Object> getStats(Long userId) {
        List<JobApplication> list = getByUserId(userId);
        Map<String, Object> stats = new LinkedHashMap<>();
        stats.put("total", list.size());

        Map<String, Long> byStatus = new LinkedHashMap<>();
        Map<String, Long> byChannel = new LinkedHashMap<>();

        for (JobApplication app : list) {
            byStatus.merge(app.getStatus() != null ? app.getStatus() : "未知", 1L, Long::sum);
            byChannel.merge(app.getChannel() != null ? app.getChannel() : "未知", 1L, Long::sum);
        }

        stats.put("byStatus", byStatus);
        stats.put("byChannel", byChannel);
        return stats;
    }
}
