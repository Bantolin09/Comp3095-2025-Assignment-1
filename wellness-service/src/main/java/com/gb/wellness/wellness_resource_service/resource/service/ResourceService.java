package com.gb.wellness.wellness_resource_service.resource.service;

import com.gb.wellness.wellness_resource_service.resource.api.ResourceRequest;
import com.gb.wellness.wellness_resource_service.resource.domain.ResourceEntity;
import com.gb.wellness.wellness_resource_service.resource.repo.ResourceRepository;
import com.gb.wellness.wellness_resource_service.resource.util.ResourceMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ResourceService {

    private final ResourceRepository repo;

    public ResourceService(ResourceRepository repo) {
        this.repo = repo;
    }

    // READ: list all resources (cached)
    @Cacheable(cacheNames = "resources:all")
    @Transactional(readOnly = true)
    public List<ResourceEntity> listAll() {
        simulateLatency(); // helps you show caching speedup in Postman
        return repo.findAll();
    }

    // READ: find by category (cached)
    @Cacheable(cacheNames = "resources:byCategory", key = "#category.toLowerCase()")
    @Transactional(readOnly = true)
    public List<ResourceEntity> findByCategory(String category) {
        simulateLatency();
        return repo.findByCategoryIgnoreCase(category);
    }

    // READ: search by keyword (cached)
    @Cacheable(cacheNames = "resources:search", key = "#q.toLowerCase()")
    @Transactional(readOnly = true)
    public List<ResourceEntity> search(String q) {
        simulateLatency();
        return repo.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(q, q);
    }

    // READ: get by ID (cached)
    @Cacheable(cacheNames = "resources:byId", key = "#id")
    @Transactional(readOnly = true)
    public ResourceEntity get(Long id) {
        simulateLatency();
        return repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Resource not found: " + id));
    }

    // CREATE: evict list/search caches so new data shows up
    @CacheEvict(cacheNames = {"resources:all", "resources:byCategory", "resources:search"}, allEntries = true)
    public ResourceEntity create(ResourceRequest req) {
        ResourceEntity e = ResourceMapper.toEntity(req);
        return repo.save(e);
    }

    // UPDATE: refresh byId cache and evict list/search caches
    @CachePut(cacheNames = "resources:byId", key = "#id")
    @CacheEvict(cacheNames = {"resources:all", "resources:byCategory", "resources:search"}, allEntries = true)
    public ResourceEntity update(Long id, ResourceRequest req) {
        ResourceEntity e = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Resource not found: " + id));
        ResourceMapper.apply(e, req);
        return repo.save(e);
    }

    // DELETE: evict all caches
    @CacheEvict(cacheNames = {"resources:byId", "resources:all", "resources:byCategory", "resources:search"}, allEntries = true)
    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new IllegalArgumentException("Resource not found: " + id);
        }
        repo.deleteById(id);
    }

    // Helper: simulate DB latency so caching improvements are obvious in Postman
    private void simulateLatency() {
        try { Thread.sleep(400); } catch (InterruptedException ignored) {}
    }
}