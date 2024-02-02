package com.intuit.interview.demo.monitor.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is used to trace the execution of methods.
 * It can be applied to methods and is retained at runtime.
 * It has an optional parameter 'message' that can be used to provide additional information about the method execution.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface TraceMethodExecution {
    /**
     * This method is used to set an additional message for the method execution.
     * It is optional and defaults to an empty string.
     *
     * @return the additional message
     */
    String message() default "";
}