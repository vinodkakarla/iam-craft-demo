package com.intuit.interview.demo.service;

import com.intuit.interview.demo.repository.db.ObjectMetadataRepository;
import com.intuit.interview.demo.repository.s3.AwsS3Repository;
import com.intuit.interview.demo.utils.JsonUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import software.amazon.awssdk.services.s3.model.S3Object;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class DocStoreServiceImplTest {

    @Mock
    private JsonUtils jsonUtils;

    @Mock
    private ObjectMetadataRepository metadataRepository;

    @Mock
    private AwsS3Repository s3Repository;

    @InjectMocks
    private DocStoreServiceImpl docStoreService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void listObjects_returnsListOfJsonObjects() {
        S3Object s3Object = S3Object.builder().key("testKey").build();
        when(s3Repository.listObjects("s3-develop")).thenReturn(Arrays.asList(s3Object));
        when(jsonUtils.getJsonFromObject(s3Object.toBuilder())).thenReturn("{\"key\":\"testKey\"}");

        List<String> result = docStoreService.listObjects();

        assertEquals(1, result.size());
    }

    @Test
    public void getObjectMetadata_returnsMetadataWhenExists() {
        String fileName = "testFile";
        S3Object s3Object = mock(S3Object.class);
        when(s3Repository.getObjectFile(any(), any())).thenReturn(Optional.of(s3Object));
        doNothing().when(metadataRepository).saveMetadata(any());//.get(fileName)).thenReturn(Optional.of("metadata"));

        String result = docStoreService.getObjectMetadata(fileName);

        //assertEquals("metadata", result);
    }

    @Test
    public void getObjectMetadata_returnsFileNotFoundWhenDoesNotExist() {
        String fileName = "testFile";
        when(s3Repository.getObjectFile(any(), any())).thenReturn(Optional.empty());

        String result = docStoreService.getObjectMetadata(fileName);

        assertEquals("File not found!", result);
    }

    @Test
    public void auditObjectMetadata_returnsAuditLog() {
        List<String> expectedLog = Arrays.asList("metadata1", "metadata2", "metadata3");

        when(metadataRepository.getMetadata()).thenReturn(expectedLog);

        List<String> result = docStoreService.auditObjectMetadata();

        assertEquals(expectedLog, result);
    }
}