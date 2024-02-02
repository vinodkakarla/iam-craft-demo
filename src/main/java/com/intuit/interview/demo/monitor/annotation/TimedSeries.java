package com.intuit.interview.demo.monitor.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is used to mark methods that should be timed for performance monitoring.
 * It can be applied to any method in a Spring Boot application.
 *
 * @Retention(RetentionPolicy.RUNTIME) This annotation is available at runtime for use by JVM and other frameworks.
 * @Target(ElementType.METHOD) This annotation can only be applied to methods.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface TimedSeries {

    /**
     * Optional message that can be used to provide additional information about the timing.
     * By default, it is an empty string.
     *
     * @return A string containing the message.
     */
    String message() default "";
}