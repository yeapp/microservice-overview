package com.patapchuk.resprocessor.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.patapchuk.resprocessor.model.Resource;

@Slf4j
@RestController
@RequestMapping("api/v1/resource-processor/")
public class ResourceProcessorController {

    @GetMapping("{key}")
    ResponseEntity<Resource> getResourcesMetaData(@PathVariable String key) {
        return null;
    }
}
