package org.ruott.consumer.test;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AnnotationProxyAop {

    @Around("@annotation(end)")
    public Object annotatedMethod(ProceedingJoinPoint joinPoint, End end) throws Throwable {
        System.out.println("方法前增强……");
        //环绕通知
        Object proceed = joinPoint.proceed();
        System.out.println("方法后增强……");
        return proceed;
    }
}
