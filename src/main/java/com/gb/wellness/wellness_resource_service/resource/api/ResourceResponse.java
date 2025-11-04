package com.gb.wellness.wellness_resource_service.resource.api;

import java.time.Instant; /**
 * DTO for outgoing responses when returning resource data.
 */
public record ResourceResponse(
        Long resourceId,
        String title,
        String description,
        String category,
        String url,
        Instant createdAt,
        Instant updatedAt
) {}
