package com.intuit.interview.demo.repository.s3;

import com.intuit.interview.demo.config.aspect.MethodTracerAspect;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.auth.credentials.AnonymousCredentialsProvider;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.core.waiters.WaiterResponse;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.waiters.S3Waiter;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * This class is a repository that handles the interaction with AWS S3.
 * It uses the AWS SDK for Java to communicate with the S3 service.
 */
@Repository
public class AwsS3Repository {

    // Logger to log the information
    private static final Logger LOGGER = Logger.getLogger(AwsS3Repository.class.getName());


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
        AwsCredentials awsCredentials = AwsBasicCredentials.create("ACCES_KEY", "SECRET_KEY");
//        AmazonS3 amazonS3 = new AmazonS3Client(awsCredentials );
//        amazonS3.createBucket("<BUCKET_NAME>");

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
     * @param objectKey  the key of the object to retrieve
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


    // Create a bucket by using a S3Waiter object
    public static void createBucket(S3Client s3Client, String bucketName) {
        try {
            S3Waiter s3Waiter = s3Client.waiter();
            CreateBucketRequest bucketRequest = CreateBucketRequest.builder()
                    .bucket(bucketName)
                    .build();

            s3Client.createBucket(bucketRequest);
            HeadBucketRequest bucketRequestWait = HeadBucketRequest.builder()
                    .bucket(bucketName)
                    .build();

            // Wait until the bucket is created and print out the response.
            WaiterResponse<HeadBucketResponse> waiterResponse = s3Waiter.waitUntilBucketExists(bucketRequestWait);
            waiterResponse.matched().response().map(String::valueOf).ifPresent(LOGGER::info);
            LOGGER.info(bucketName + " is ready");

        } catch (S3Exception e) {
            LOGGER.warning(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }

    public static void listBuckets(S3Client s3, String bucket) {
        // snippet-start:[s3.java2.s3_bucket_ops.list_bucket]
        // List buckets
        ListBucketsRequest listBucketsRequest = ListBucketsRequest.builder().build();
        ListBucketsResponse listBucketsResponse = s3.listBuckets(listBucketsRequest);
        listBucketsResponse.buckets().stream().forEach(x -> LOGGER.info(x.name()));
        // snippet-end:[s3.java2.s3_bucket_ops.list_bucket]
    }

    public static void deleteEmptyBucket(S3Client s3, String bucket) {
        // Delete empty bucket.
        // snippet-start:[s3.java2.s3_bucket_ops.delete_bucket]
        DeleteBucketRequest deleteBucketRequest = DeleteBucketRequest.builder()
                .bucket(bucket)
                .build();

        s3.deleteBucket(deleteBucketRequest);
        s3.close();
        // snippet-end:[s3.java2.s3_bucket_ops.delete_bucket]
    }


    public static void deleteObjectsInBucket(S3Client s3, String bucket) {
        try {
            // To delete a bucket, all the objects in the bucket must be deleted first.
            ListObjectsV2Request listObjectsV2Request = ListObjectsV2Request.builder()
                    .bucket(bucket)
                    .build();
            ListObjectsV2Response listObjectsV2Response;

            do {
                listObjectsV2Response = s3.listObjectsV2(listObjectsV2Request);
                for (S3Object s3Object : listObjectsV2Response.contents()) {
                    DeleteObjectRequest request = DeleteObjectRequest.builder()
                            .bucket(bucket)
                            .key(s3Object.key())
                            .build();
                    s3.deleteObject(request);
                }
            } while (listObjectsV2Response.isTruncated());
            DeleteBucketRequest deleteBucketRequest = DeleteBucketRequest.builder().bucket(bucket).build();
            s3.deleteBucket(deleteBucketRequest);

        } catch (S3Exception e) {
            LOGGER.warning(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
}