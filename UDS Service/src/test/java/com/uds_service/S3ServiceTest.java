package com.uds_service;

import com.uds_service.service.S3Service;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import software.amazon.awssdk.services.s3.S3Client;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class S3ServiceTest {

    private final S3Client s3Client = Mockito.mock(S3Client.class);
    private final S3Service s3Service = new S3Service(s3Client);

    @Test
    void testSearchFiles() {
        String userName = "sandy";
        String searchTerm = "logistics";
        List<String> results = s3Service.searchFiles(userName, searchTerm);
        assertTrue(results.isEmpty(), "Expected no search results for non-existent bucket or user data");
    }
}
