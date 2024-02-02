package com.intuit.interview.demo.repository.s3;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.S3Object;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AwsS3RepositoryTest {

    @Mock
    private S3Client s3Client;
    @Mock
    private ListObjectsV2Response listObjectsV2Response;

    @InjectMocks
    @Spy
    private AwsS3Repository awsS3Repository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

     @Test
    public void listObjects_returnsListOfS3Objects() {
        String bucketName = "testBucket";
        S3Object s3Object = S3Object.builder().key("testKey").build();
        when(awsS3Repository.getObjResponse(any()))
                .thenReturn(listObjectsV2Response);
        when(listObjectsV2Response.contents())
                .thenReturn(Collections.singletonList(s3Object));

        List<S3Object> result = awsS3Repository.listObjects(bucketName);

        assertEquals(1, result.size());
        assertEquals(s3Object, result.get(0));
    }

     @Test
    public void getObjectFile_returnsS3ObjectIfExists() {
        String bucketName = "testBucket";
        String objectKey = "testKey";
        S3Object s3Object = S3Object.builder().key(objectKey).build();
        Consumer<ListObjectsV2Request.Builder> listObjectsV2Request = mock();
        when(awsS3Repository.getObjResponse(any()))
                .thenReturn(listObjectsV2Response);
        when(listObjectsV2Response.contents())
                .thenReturn(Collections.singletonList(s3Object));

        Optional<S3Object> result = awsS3Repository.getObjectFile(bucketName, objectKey);

        assertTrue(result.isPresent());
        assertEquals(s3Object, result.get());
    }

     @Test
    public void getObjectFile_returnsEmptyOptionalIfDoesNotExist() {
        String bucketName = "testBucket";
        String objectKey = "testKey";
        when(awsS3Repository.getObjResponse(any()))
                .thenReturn(listObjectsV2Response);
        when(listObjectsV2Response.contents())
                .thenReturn(Collections.emptyList());

        Optional<S3Object> result = awsS3Repository.getObjectFile(bucketName, objectKey);

        assertTrue(result.isEmpty());
    }
}