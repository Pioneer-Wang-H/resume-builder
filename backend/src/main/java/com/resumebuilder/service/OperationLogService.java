package com.resumebuilder.service;

import cn.dev33.satoken.stp.StpUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.resumebuilder.entity.OperationLog;
import com.resumebuilder.mapper.OperationLogMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
@RequiredArgsConstructor
public class OperationLogService {

    private final OperationLogMapper operationLogMapper;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Async
    public void save(String module, String operation, Object[] args) {
        try {
            OperationLog entity = new OperationLog();
            entity.setModule(module);
            entity.setOperation(operation);
            entity.setUserId(StpUtil.getLoginIdAsLong());
            entity.setParams(objectMapper.writeValueAsString(args));

            ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attrs != null) {
                HttpServletRequest request = attrs.getRequest();
                String ip = request.getHeader("X-Forwarded-For");
                if (ip == null || ip.isEmpty()) ip = request.getRemoteAddr();
                entity.setIp(ip);
            }
            operationLogMapper.insert(entity);
        } catch (Exception ignored) {}
    }
}
