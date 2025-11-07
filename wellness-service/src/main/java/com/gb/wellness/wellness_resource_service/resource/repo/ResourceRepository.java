package com.gb.wellness.wellness_resource_service.resource.repo;

import com.gb.wellness.wellness_resource_service.resource.domain.ResourceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResourceRepository extends JpaRepository<ResourceEntity, Long> {

    // Filter resources by category (case-insensitive)
    List<ResourceEntity> findByCategoryIgnoreCase(String category);

    // Keyword search in title OR description (case-insensitive)
    List<ResourceEntity> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String title, String description);
}