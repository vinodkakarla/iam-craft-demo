package com.intuit.interview.demo.config.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.logging.Logger;

@Component
@Aspect
public class TimedSeriesAspect {
    private static final Logger LOGGER = Logger.getLogger(TimedSeriesAspect.class.getName());


    @Around("@annotation(com.intuit.interview.demo.monitor.annotation.TimedSeries)")
    public Object logTimedMetric(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
        String className = methodSignature.getDeclaringType().getSimpleName();
        String methodName = methodSignature.getMethod().getName();
        String params = Arrays.stream(methodSignature.getParameterTypes()).map(Class::getSimpleName).reduce((a, b) -> a + ", " + b).orElse("");
        Instant startTime = Instant.now();
        Object result = proceedingJoinPoint.proceed();
        long elapsedTime = Duration.between(startTime, Instant.now()).toMillis();
        LOGGER.info(String.format("Class %s with Method %s invoked with params %s. Execution Time: %sms",
                className, methodName, params, elapsedTime));
        return result;
    }


}
