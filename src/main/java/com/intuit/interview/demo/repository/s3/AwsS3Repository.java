package com.intuit.interview.demo.repository.s3;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.auth.credentials.AnonymousCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.S3Object;

import java.util.List;
import java.util.Optional;

/**
 * This class is a repository that handles the interaction with AWS S3.
 * It uses the AWS SDK for Java to communicate with the S3 service.
 */
@Repository
public class AwsS3Repository {

    // The S3 client used to interact with AWS S3
    private S3Client s3Client;

    // The AWS region where the S3 bucket is located
    @Value("${aws.s3.region:ap-northeast-1}")
    private Region region;

    /**
     * This method initializes the S3 client.
     * It is called after the properties have been set.
     */
    @PostConstruct
    public void init() {
        s3Client = S3Client.builder()
                .region(region)
                .credentialsProvider(AnonymousCredentialsProvider.create())
                .build();
    }

    /**
     * This method lists all the objects in the given S3 bucket.
     *
     * @param bucketName the name of the S3 bucket
     * @return a list of S3 objects
     */
    public List<S3Object> listObjects(String bucketName) {
        return getObjResponse(bucketName)
                .contents();
    }

    /**
     * This method retrieves a specific object from the given S3 bucket.
     *
     * @param bucketName the name of the S3 bucket
     * @param objectKey the key of the object to retrieve
     * @return an Optional containing the S3 object if it exists, or an empty Optional if it does not
     */
    public Optional<S3Object> getObjectFile(String bucketName, String objectKey) {
        return getObjResponse(bucketName)
                .contents().stream()
                .filter(c -> c.key().equalsIgnoreCase(objectKey))
                .findFirst();
    }

    /**
     * This method sets the S3 client used to interact with AWS S3.
     *
     * @param s3Client the S3 client to use
     */
    void setS3Client(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    ListObjectsV2Response getObjResponse(String bucketName) {
        return s3Client.listObjectsV2(builder -> builder.bucket(bucketName));
    }

}