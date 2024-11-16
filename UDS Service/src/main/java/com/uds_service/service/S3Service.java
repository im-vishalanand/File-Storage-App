package com.uds_service.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class S3Service {

    private final S3Client s3Client;
    @Value("${s3.bucket.name}")
    private String bucketName;

    public S3Service(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    // Search for files in the user's directory based on a search term
    public List<String> searchFiles(String userName, String searchTerm) {
        String userFolder = userName + "/";
        ListObjectsV2Request request = ListObjectsV2Request.builder()
                .bucket(bucketName)
                .prefix(userFolder)
                .build();

        ListObjectsV2Response result = s3Client.listObjectsV2(request);
        return result.contents().stream()
                .map(S3Object::key)
                .filter(key -> key.contains(searchTerm))
                .collect(Collectors.toList());
    }

    // Download a file from the user's folder
    public ResponseInputStream<GetObjectResponse> downloadFile(String userName, String fileName) {
        String key = userName + "/" + fileName;
        GetObjectRequest request = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();
        return s3Client.getObject(request);
    }

    // Optional: Upload a file to the user's folder
    public void uploadFile(String userName, String fileName, byte[] fileData) throws IOException {
        String key = userName + "/" + fileName;
        s3Client.putObject(PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build(), RequestBody.fromBytes(fileData));
    }
}
