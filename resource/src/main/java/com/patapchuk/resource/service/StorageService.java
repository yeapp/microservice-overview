package com.patapchuk.resource.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.patapchuk.resource.enums.Status;
import com.patapchuk.resource.exceptions.ResourceInternalServerErrorException;
import com.patapchuk.resource.exceptions.ResourceRequestException;
import com.patapchuk.resource.model.Resource;
import com.patapchuk.resource.repository.ResourceRepository;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Objects;

@Slf4j
@Service
@PropertySource("classpath:application.yml")
public class StorageService {

    @Autowired
    private AmazonS3 amazonS3;

    @Autowired
    ResourceRepository resourceRepository;

    @Value("${song.storage.bucket}")
    private String bucket;

    public Resource create(MultipartFile multipartFile) {
        if (!Objects.equals(multipartFile.getContentType(), "audio/mpeg")) {
            throw new ResourceRequestException("Validation error or request body is invalid MP3");
        }
        var convertedFile = convertMultiPartFileToFile(multipartFile);
        var fileName = generateUniqueFileName(multipartFile.getOriginalFilename());
        log.info("Loading {} file to S3 Bucket...", fileName);
        try {
            amazonS3.putObject(bucket, fileName, multipartFile.getInputStream(), getMetadata(multipartFile));
        } catch (AmazonServiceException | IOException e) {
            log.info("Failed to load the {} file to S3 Bucket: {}", fileName, e.getMessage());
            createResource(convertedFile, fileName, Status.FAILED.toString());
            throw new ResourceRequestException("Failed to load the file to S3 Bucket");
        }
        return createResource(convertedFile, fileName, Status.CREATED.toString());
    }

    private ObjectMetadata getMetadata(MultipartFile file) {
        var meta = new ObjectMetadata();
        meta.setContentLength(file.getSize());
        meta.setContentType(file.getContentType());
        return meta;
    }

    private Resource createResource(File file, String fileName, String status) {
        var resource = Resource.builder()
                .contentLength(file.getTotalSpace())
                .status(status)
                .fileName(fileName)
                .build();
        return resourceRepository.save(resource);
    }

    public void deleteFile(Resource resource) {
        log.info("Deleting AWS S3 Bucket {} fileName", resource.getFileName());
        amazonS3.deleteObject(bucket, resource.getFileName());
        resource.setStatus(Status.DELETED.toString());
        resourceRepository.save(resource);
    }

    public byte[] downloadFile(String fileName) {
        S3Object s3Object = amazonS3.getObject(bucket, fileName);
        S3ObjectInputStream inputStream = s3Object.getObjectContent();
        try {
            return IOUtils.toByteArray(inputStream);
        } catch (IOException e) {
            throw new ResourceInternalServerErrorException(
                    String.format("Internal error is happened: %s", e.getMessage())
            );
        }
    }

    private String generateUniqueFileName(String filename) {
        long millis = System.currentTimeMillis();
        String datetime = new Date().toString();
        datetime = datetime.replace(" ", "");
        datetime = datetime.replace(":", "");
        return String.format("%s_%s_%s", datetime, millis, filename);
    }

    private File convertMultiPartFileToFile(MultipartFile file) {
        File convertedFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
        } catch (IOException e) {
            log.error("Error converting multipartFile to file: ", e);
        }
        return convertedFile;
    }


    public void createBucket(String name) {
        if (!amazonS3.doesBucketExistV2(name)) {
            amazonS3.createBucket(name);
        }
    }

    public void deleteBucket(String name) {
        if (amazonS3.doesBucketExistV2(name)) {
            amazonS3.deleteBucket(name);
        }
    }
}
