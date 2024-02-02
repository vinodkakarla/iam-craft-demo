package com.intuit.interview.demo.utils;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * This class is a utility that handles the logging of audit information.
 * It uses the java.util.logging.Logger to log messages to a file named "audit.log".
 * It also provides a method to retrieve the last few lines of the log.
 */
@Component
public class AuditLogger {

    // The Logger used to log messages
    private static final Logger LOGGER = Logger.getLogger(AuditLogger.class.getName());

    // The name of the log file
    private static final String FILE_NAME = "audit.log";

    /**
     * This constructor initializes the AuditLogger.
     * It sets up a FileHandler to log messages to the "audit.log" file.
     */
    public AuditLogger() {
        try {
            FileHandler fileHandler = new FileHandler(FILE_NAME, true);
            fileHandler.setFormatter(new SimpleFormatter());
            LOGGER.addHandler(fileHandler);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method logs the given message.
     *
     * @param message the message to log
     */
    public void log(String message) {
        LOGGER.info(message);
    }

    /**
     * This method retrieves the last n lines of the log.
     *
     * @param n the number of lines to retrieve
     * @return a LinkedList containing the last n lines of the log
     */
    public List<String> tail(int n) {
        return tail(n, 0);
    }

    /**
     * This method retrieves the last n lines of the log, starting from the given offset.
     *
     * @param n the number of lines to retrieve
     * @param offset the offset from the end of the log to start retrieving lines
     * @return a LinkedList containing the last n lines of the log, starting from the given offset
     */
    public List<String> tail(int n, int offset) {
        LinkedList<String> result = new LinkedList<>();
        try (RandomAccessFile file = new RandomAccessFile(FILE_NAME, "r")) {
            long fileLength = file.length() - 1;
            StringBuilder sb = new StringBuilder();
            int lineCount = 0;
            for(long filePointer = fileLength; filePointer != -1; filePointer--){
                file.seek(filePointer);
                int readByte = file.readByte();

                if(readByte == 0xA || readByte == 0xD) {
                    if(filePointer < fileLength - 1) {
                        lineCount++;
                        if(lineCount == 5*n + offset) {
                            break;
                        }
                    }
                }
                sb.append((char) readByte);
            }

            String[] lines = Arrays.stream(sb.reverse().toString()
                            .split("\n")).filter(str -> str.startsWith("INFO"))
                    .map(str -> str.replace("INFO: ", "").replace("\\r", ""))
                    .limit(n + offset)
                    .toArray(String[]::new);
            for(int i = offset; i < lines.length; i++) {
                result.addFirst(lines[i]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}