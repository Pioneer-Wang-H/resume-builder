package com.resumebuilder.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.resumebuilder.entity.OperationLog;
import com.resumebuilder.mapper.OperationLogMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OperationLogService {

    private static final Logger log = LoggerFactory.getLogger(OperationLogService.class);
    private final OperationLogMapper operationLogMapper;
    private final ObjectMapper objectMapper;

    @Async
    public void save(String module, String operation, Object[] args, Long userId, String ip) {
        try {
            OperationLog entity = new OperationLog();
            entity.setModule(module);
            entity.setOperation(operation);
            entity.setUserId(userId);
            entity.setParams(objectMapper.writeValueAsString(args));
            entity.setIp(ip);
            operationLogMapper.insert(entity);
        } catch (Exception e) {
            log.error("保存操作日志失败", e);
        }
    }
}
