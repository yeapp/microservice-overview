package com.patapchuk.resource.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.patapchuk.resource.enums.Status;
import com.patapchuk.resource.exceptions.CheckedFunction;
import com.patapchuk.resource.exceptions.ResourceInternalServerErrorException;
import com.patapchuk.resource.exceptions.ResourceNotFoundException;
import com.patapchuk.resource.model.Resource;
import com.patapchuk.resource.repository.ResourceRepository;

import java.util.Arrays;
import java.util.Optional;
import java.util.List;
import java.util.LinkedList;
import java.util.Collections;
import java.util.function.Function;

@Slf4j
@Service
public record ResourceService(StorageService storageService,
                              ResourceRepository resourceRepository,
                              RestTemplate restTemplate) {

    public ResponseEntity<?> get(String id) {
        Optional<Resource> resource = findByIdAndStatus(Long.parseLong(id));
        if (resource.isEmpty()) {
            throw new ResourceNotFoundException(String.format("Resource doesn't exist with given ID = %s", id));
        }
        byte[] data = storageService.downloadFile(resource.get().getFileName());
        return ResponseEntity
                .ok()
                .contentLength(data.length)
                .header("Content-type", "application/octet-stream")
                .header("Content-disposition", "attachment; filename=\"" + resource.get().getFileName() + "\"")
                .body(new ByteArrayResource(data));
    }

    public ResponseEntity<String> delete(String id) {
        List<Long> idsList = Arrays
                .stream(id.split(","))
                .map(wrap(s -> Long.parseLong(s.trim())))
                .toList();

        List<Long> deletedResources = new LinkedList<>();
        idsList.forEach((i) -> {
            Optional<Resource> resource = findByIdAndStatus(i);
            if (resource.isPresent()) {
                try {
                    storageService.deleteFile(resource.get());
                } catch (Exception ex) {
                    log.info("Error happened while deleting " + resource.get() + " from S3 bucket {}", ex.getMessage() );
                }
                deletedResources.add(i);
            }
        });
        return ResponseEntity
                .ok()
                .body(Collections.singletonMap("ids", deletedResources.toString()).toString());
    }

    public Optional<Resource> findByIdAndStatus(Long id) {
        return resourceRepository.findByIdAndStatus(id, Status.CREATED.toString());
    }

    public static <T, R> Function<T, R> wrap(CheckedFunction<T, R> checkedFunction) {
        return t -> {
            try {
                return checkedFunction.apply(t);
            } catch (Exception e) {
                throw new ResourceInternalServerErrorException("Internal server error is happened");
            }
        };
    }
}

