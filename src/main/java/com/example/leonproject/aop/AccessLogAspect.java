package com.example.leonproject.aop;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;

@Slf4j
@Aspect
@Component
public class AccessLogAspect {

    @Pointcut("execution(public * com.example.leonproject.controller.*.*(..))&& " +
            "!(execution(public * com.example.leonproject.controller.LoginController.*(..)) || execution(public * com.example.leonproject.controller.RegistrationController.*(..)))")
    public void accessLogPointcut() {
    }

    @Around("accessLogPointcut()")
    public Object accessLog(ProceedingJoinPoint joinPoint) throws Throwable {

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String url = request.getRequestURL().toString();
        String method = request.getMethod();
        String params = Arrays.toString(joinPoint.getArgs());

        log.debug("Access Log - URL: {}, Method: {}, Params: {}", url, method, params);
        log.info("Access Log - URL: {}, Method: {}, Params: {}", url, method, params);

        return joinPoint.proceed();
    }
}
