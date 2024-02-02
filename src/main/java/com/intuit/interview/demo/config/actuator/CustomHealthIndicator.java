package com.intuit.interview.demo.config.actuator;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class CustomHealthIndicator implements HealthIndicator {

    private final String message_key = "Custom Service";

    @Override
    public Health health() {
        if (!isRunningServiceB()) {
            return Health.down().withDetail(message_key, "Not Available").build();
        }
        return Health.up().withDetail(message_key, "Available").build();
    }

    private Boolean isRunningServiceB() {
        Boolean isRunning = true;
        // Your logic here
        return isRunning;
    }
}