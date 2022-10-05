package com.patapchuk.resource.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.patapchuk.resource.model.Resource;

import java.util.Optional;

public interface ResourceRepository extends JpaRepository<Resource, Long> {
    Optional<Resource> findByIdAndStatus(Long id, String status);
}
