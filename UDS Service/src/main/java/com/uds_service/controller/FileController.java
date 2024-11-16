package com.uds_service.controller;

import com.uds_service.service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class FileController {

    @Autowired
    private S3Service s3Service;

    @GetMapping("/search")
    public ResponseEntity<List<String>> searchFiles(@RequestParam String userName, @RequestParam String searchTerm) {
        List<String> files = s3Service.searchFiles(userName, searchTerm);
        return ResponseEntity.ok(files);
    }

    @GetMapping("/download")
    public ResponseEntity<InputStreamResource> downloadFile(@RequestParam String userName, @RequestParam String fileName) {
        var fileStream = s3Service.downloadFile(userName, fileName);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new InputStreamResource(fileStream));
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam String userName, @RequestParam String fileName, @RequestBody byte[] fileData) {
        try {
            s3Service.uploadFile(userName, fileName, fileData);
            return ResponseEntity.ok("File uploaded successfully");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("File upload failed");
        }
    }
}
