package com.patapchuk.resource.rest;

import com.sun.istack.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.multipart.MultipartFile;
import com.patapchuk.resource.service.ResourceService;
import com.patapchuk.resource.service.StorageService;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("api/v1/resources")
@Validated
public record ResourceController(StorageService storageService, ResourceService resourceService) {

    @PostMapping
    public ResponseEntity<Map<String, String>> createResource(@RequestParam("file") @NotNull MultipartFile multipartFile) {
        log.info("Create resource {}", multipartFile);
        return ResponseEntity
                .ok(Collections.singletonMap("id", storageService.create(multipartFile).getId().toString()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getResource(@PathVariable @NotNull String id) {
        log.info("Get ByteArray resource by {}", id);
        return resourceService.get(id);
    }

    @DeleteMapping
    public ResponseEntity<String> deleteResource(@RequestParam("id") String id) {
        log.info("delete resource by {}", id);
        return resourceService.delete(id);
    }

    @PostMapping("/create-bucket")
    public String createBucket(@RequestParam("bucketName") String bucketName) {
        storageService.createBucket(bucketName);
        return bucketName + " successfully created";
    }

    @DeleteMapping("deleteBucket")
    public String deleteBucket(@RequestParam("bucketName") String bucketName) {
        storageService.deleteBucket(bucketName);
        return bucketName + " successfully deleted";
    }

}
