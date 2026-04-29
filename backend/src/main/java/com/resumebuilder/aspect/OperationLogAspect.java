package com.resumebuilder.aspect;

import com.resumebuilder.service.OperationLogService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class OperationLogAspect {

    private final OperationLogService operationLogService;

    @Around("@annotation(com.resumebuilder.annotation.OperationLog)")
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = joinPoint.proceed();

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        com.resumebuilder.annotation.OperationLog opLog =
                signature.getMethod().getAnnotation(com.resumebuilder.annotation.OperationLog.class);
        if (opLog != null) {
            operationLogService.save(opLog.module(), opLog.operation(), joinPoint.getArgs());
        }
        return result;
    }
}
