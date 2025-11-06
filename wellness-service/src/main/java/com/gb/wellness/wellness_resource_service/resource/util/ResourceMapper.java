package com.gb.wellness.wellness_resource_service.resource.util;

import com.gb.wellness.wellness_resource_service.resource.api.ResourceRequest;
import com.gb.wellness.wellness_resource_service.resource.api.ResourceResponse;
import com.gb.wellness.wellness_resource_service.resource.domain.ResourceEntity;

public class ResourceMapper {

    // Convert a request DTO into a new entity
    public static ResourceEntity toEntity(ResourceRequest req) {
        ResourceEntity e = new ResourceEntity();
        apply(e, req);
        return e;
    }

    // Apply updates from a request DTO onto an existing entity
    public static void apply(ResourceEntity e, ResourceRequest req) {
        e.setTitle(req.getTitle());
        e.setDescription(req.getDescription());
        e.setCategory(req.getCategory());
        e.setUrl(req.getUrl());
    }

    // Convert an entity into a response DTO
    public static ResourceResponse toResponse(ResourceEntity e) {
        return new ResourceResponse(
                e.getResourceId(),
                e.getTitle(),
                e.getDescription(),
                e.getCategory(),
                e.getUrl(),
                e.getCreatedAt(),
                e.getUpdatedAt()
        );
    }
}