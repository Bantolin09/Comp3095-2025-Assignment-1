package com.gb.wellness.wellness_resource_service.resource.api;

import com.gb.wellness.wellness_resource_service.resource.domain.ResourceEntity;
import com.gb.wellness.wellness_resource_service.resource.service.ResourceService;
import com.gb.wellness.wellness_resource_service.resource.util.ResourceMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/resources")
public class ResourceController {

    private final ResourceService service;

    public ResourceController(ResourceService service) {
        this.service = service;
    }

    @GetMapping("/resources")
    public ResponseEntity<List<ResourceResponse>> getAllResources() {
        try {
            List<ResourceResponse> resources = service.listAll().stream()
                    .map(ResourceMapper::toResponse)
                    .toList();
            return ResponseEntity.ok(resources);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping
    public List<ResourceResponse> listAll() {
        return service.listAll().stream()
                .map(ResourceMapper::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public ResourceResponse get(@PathVariable Long id) {
        return ResourceMapper.toResponse(service.get(id));
    }

    @GetMapping("/category/{category}")
    public List<ResourceResponse> byCategory(@PathVariable String category) {
        return service.findByCategory(category).stream()
                .map(ResourceMapper::toResponse)
                .toList();
    }

    @GetMapping("/search")
    public List<ResourceResponse> search(@RequestParam String q) {
        return service.search(q).stream()
                .map(ResourceMapper::toResponse)
                .toList();
    }

    @PostMapping
    public ResponseEntity<ResourceResponse> create(@RequestBody ResourceRequest req) {
        ResourceEntity saved = service.create(req);
        return ResponseEntity.created(URI.create("/api/resources/" + saved.getResourceId()))
                .body(ResourceMapper.toResponse(saved));
    }

    @PutMapping("/{id}")
    public ResourceResponse update(@PathVariable Long id, @RequestBody ResourceRequest req) {
        return ResourceMapper.toResponse(service.update(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    // ✅ NEW: GET /api/resources/for-goal/{goal}
    @GetMapping("/for-goal/{goal}")
    public List<ResourceResponse> getResourcesForGoal(@PathVariable String goal) {
        return service.findByCategory(goal).stream()
                .map(ResourceMapper::toResponse)
                .toList();
    }

    // ✅ NEW: GET /api/resources/for-event/{eventType}
    @GetMapping("/for-event/{eventType}")
    public List<ResourceResponse> getResourcesForEvent(@PathVariable String eventType) {
        return service.findByCategory(eventType).stream()
                .map(ResourceMapper::toResponse)
                .toList();
    }
}