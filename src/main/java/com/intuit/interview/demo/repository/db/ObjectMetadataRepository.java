package com.intuit.interview.demo.repository.db;

import com.intuit.interview.demo.utils.AuditLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * This class is a repository that handles the storage and retrieval of object metadata.
 * It uses an AuditLogger to log the metadata and retrieve the last few lines of logs.
 */
@Repository
public class ObjectMetadataRepository {

    // The AuditLogger used to log and retrieve metadata
    @Autowired
    private AuditLogger auditLogger;

    // The maximum number of lines to retrieve from the logs
    private final static int MAX_LINES = 25;

    /**
     * This method saves the given metadata by logging it using the AuditLogger.
     *
     * @param metadata the metadata to save
     */
    public void saveMetadata(String metadata) {
        auditLogger.log(metadata);
    }

    /**
     * This method retrieves the last few lines of metadata from the logs.
     *
     * @return a list of the last few lines of metadata
     */
    public List<String> getMetadata() {
        return auditLogger.tail(MAX_LINES);
    }
}