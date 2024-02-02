package com.intuit.interview.demo.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AuditLoggerTest {

    private AuditLogger auditLogger;

    @BeforeEach
    public void setUp() {
        auditLogger = new AuditLogger();
    }

    @Test
    public void log_addsMessageToLogFile() {
        String message = "Test message";

        auditLogger.log(message);

        List<String> logLines = auditLogger.tail(1);
        assertTrue(logLines.size() > 0);
    }

    @Test
    public void tail_returnsLastNLines() {
        String message1 = "Test message 1";
        String message2 = "Test message 2";
        String message3 = "Test message 3";

        auditLogger.log(message1);
        auditLogger.log(message2);
        auditLogger.log(message3);

        List<String> logLines = auditLogger.tail(2);

        assertEquals(2, logLines.size());
    }

    @Test
    public void tail_withOffset_returnsCorrectLines() {
        String message1 = "Test message 1";
        String message2 = "Test message 2";
        String message3 = "Test message 3";
        String message4 = "Test message 4";

        auditLogger.log(message1);
        auditLogger.log(message2);
        auditLogger.log(message3);
        auditLogger.log(message4);

        List<String> logLines = auditLogger.tail(2, 1);

        assertEquals(2, logLines.size());
    }
}