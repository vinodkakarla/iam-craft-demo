package com.intuit.interview.demo.config.aspect;

import com.intuit.interview.demo.monitor.annotation.TraceMethodExecution;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.logging.Logger;

/**
 * This class is used to trace the execution of methods.
 * It uses Aspect Oriented Programming (AOP) to intercept method calls and log their execution time.
 */
@Component
@Aspect
public class MethodTracerAspect {

    // Logger to log the information
    private static final Logger LOGGER = Logger.getLogger(MethodTracerAspect.class.getName());

    /**
     * This method is an advice that gets executed around the methods annotated with @TraceMethodExecution.
     * It logs the start time, end time and execution time of the method.
     *
     * @param proceedingJoinPoint provides information about the method being executed
     * @return the result of the method execution
     * @throws Throwable if the method execution throws an exception
     */
    @Around("@annotation(com.intuit.interview.demo.monitor.annotation.TraceMethodExecution)")
    public Object logExecutionTime(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        // Get the method signature
        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
        // Get the class name
        String className = methodSignature.getDeclaringType().getSimpleName();
        // Get the method name
        String methodName = methodSignature.getMethod().getName();
        // Get the start time
        Instant startTime = Instant.now();
        // Log the start of method execution
        LOGGER.info(String.format("Class Name: %s, Method Name: %s - Started execution", className, methodName));
        // Proceed with the method execution
        Object result = proceedingJoinPoint.proceed();
        // Get the additional message from the annotation
        String additionalMessage = methodSignature.getMethod().getAnnotation(TraceMethodExecution.class).message();
        // Calculate the elapsed time
        long elapsedTime = Duration.between(startTime, Instant.now()).toMillis();
        // Log the end of method execution
        LOGGER.info(String.format("Class Name: %s, Method Name: %s - Ended execution", className, methodName));
        // Log the additional message and elapsed time
        LOGGER.info(String.format("Class Name: %s, Method Name: %s, Additional Message: %s, Elapsed Time: %sms",
                className, methodName, additionalMessage, elapsedTime));
        // Return the result of the method execution
        return result;
    }

}