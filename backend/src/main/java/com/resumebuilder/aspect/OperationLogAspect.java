package com.resumebuilder.aspect;

import cn.dev33.satoken.stp.StpUtil;
import com.resumebuilder.service.OperationLogService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
@RequiredArgsConstructor
public class OperationLogAspect {

    private static final Logger log = LoggerFactory.getLogger(OperationLogAspect.class);
    private final OperationLogService operationLogService;

    @Around("@annotation(com.resumebuilder.annotation.OperationLog)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = joinPoint.proceed();

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        com.resumebuilder.annotation.OperationLog opLog =
                signature.getMethod().getAnnotation(com.resumebuilder.annotation.OperationLog.class);
        log.info("AOP 拦截到 @OperationLog: module={}, operation={}", opLog.module(), opLog.operation());
        if (opLog != null) {
            try {
                Long userId = StpUtil.getLoginIdAsLong();
                String ip = getClientIp();
                log.info("保存操作日志: userId={}, ip={}", userId, ip);
                operationLogService.save(opLog.module(), opLog.operation(), joinPoint.getArgs(), userId, ip);
            } catch (Exception e) {
                log.error("切面捕获 userId/IP 失败", e);
            }
        }
        return result;
    }

    private String getClientIp() {
        try {
            ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attrs != null) {
                HttpServletRequest request = attrs.getRequest();
                String ip = request.getHeader("X-Forwarded-For");
                if (ip == null || ip.isEmpty()) ip = request.getRemoteAddr();
                return ip;
            }
        } catch (Exception ignored) {}
        return null;
    }
}
