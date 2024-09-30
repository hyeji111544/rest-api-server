package com.spring.demo.core;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
@Aspect
@Component
public class AgentHandler {
    @Before("execution(* com.spring.demo.user.UserController..*(..))")
    public void logClientAgent(JoinPoint jp) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();
        String clientAgent = request.getHeader("User-Agent");
        // sout 말고 log 로 찍기
        log.info("clientAgen : " + clientAgent);
    }
}
