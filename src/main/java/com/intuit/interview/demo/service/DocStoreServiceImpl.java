package com.intuit.interview.demo.service;

import com.intuit.interview.demo.monitor.annotation.TimedSeries;
import com.intuit.interview.demo.monitor.annotation.TraceMethodExecution;
import com.intuit.interview.demo.repository.db.ObjectMetadataRepository;
import com.intuit.interview.demo.repository.s3.AwsS3Repository;
import com.intuit.interview.demo.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.S3Object;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * This class is a service that handles the interaction with the document store.
 * It uses the AwsS3Repository to interact with AWS S3 and the ObjectMetadataRepository to store and retrieve object metadata.
 * It also uses the JsonUtils to convert objects to JSON.
 */
@Service
public class DocStoreServiceImpl {

    // Message to return when a file is not found
    private final static String FILE_NOT_FOUND = "File not found!";

    // The JsonUtils used to convert objects to JSON
    @Autowired
    private JsonUtils jsonUtils;

    // The ObjectMetadataRepository used to store and retrieve object metadata
    @Autowired
    private ObjectMetadataRepository metadataRepository;

    // The AwsS3Repository used to interact with AWS S3
    @Autowired
    private AwsS3Repository s3Repository;

    // The type of the S3Object.Builder
    private final Class<? extends S3Object.Builder> type = S3Object.serializableBuilderClass();

    // The name of the S3 bucket
    private final String bucketName = "s3-develop";

    /**
     * This method lists all objects in the S3 bucket.
     * It traces the execution of the method and returns a list of JSON representations of the objects.
     *
     * @return a list of JSON representations of the objects
     */
    @TraceMethodExecution(message = "Listing Objects")
    @TimedSeries(message = "List Objects")
    public List<String> listObjects() {
        return s3Repository.listObjects(bucketName)
                .stream().map(x -> type.cast(x.toBuilder()))
                .map(jsonUtils::getJsonFromObject)
                .collect(Collectors.toList());
    }

    /**
     * This method retrieves the metadata of a specific object.
     * It traces the execution of the method, caches the result, and saves the metadata in the ObjectMetadataRepository.
     * If the metadata is not found, it returns a message indicating that the file was not found.
     *
     * @param fileName the name of the object to retrieve the metadata of
     * @return the metadata of the object, or a message indicating that the file was not found
     */
    @TraceMethodExecution(message = "Get Object Metadata")
    @TimedSeries(message = "Get Object Metadata")
    @Cacheable("doc-store-metadata")
    public String getObjectMetadata(String fileName) {
        Optional<String> metadata = s3Repository.getObjectFile(bucketName, fileName)
                .map(x -> type.cast(x.toBuilder()))
                .map(jsonUtils::getJsonFromObject);
        //Optional<String> metadata = Optional.of("metadata");
        if(metadata.isPresent()) {
            metadataRepository.saveMetadata(metadata.get());
        }
        return metadata.orElseGet(() -> FILE_NOT_FOUND);
    }

    /**
     * This method retrieves the audit log of object metadata.
     * It traces the execution of the method and returns a list of the last few lines of the audit log.
     *
     * @return a list of the last few lines of the audit log
     */
    @TraceMethodExecution(message = "Audit Object Metadata")
    @TimedSeries(message = "Audit Object Metadata")
    public List<String> auditObjectMetadata() {
        return metadataRepository.getMetadata();
        //return Arrays.asList("metadata1", "metadata2", "metadata3");
    }

}