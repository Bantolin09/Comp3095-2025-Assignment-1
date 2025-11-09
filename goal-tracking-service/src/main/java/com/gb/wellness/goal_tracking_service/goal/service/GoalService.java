package com.gb.wellness.goal_tracking_service.goal.service;

import com.gb.wellness.goal_tracking_service.goal.api.GoalRequest;
import com.gb.wellness.goal_tracking_service.goal.api.ResourceResponse;
import com.gb.wellness.goal_tracking_service.goal.domain.Goal;
import com.gb.wellness.goal_tracking_service.goal.domain.GoalStatus;
import com.gb.wellness.goal_tracking_service.goal.exception.GoalNotFoundException;
import com.gb.wellness.goal_tracking_service.goal.repository.GoalRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

@Service
public class GoalService {

    private final GoalRepository repository;
    private final RestTemplate restTemplate;

    @Value("${wellness.resource.service.url:http://localhost:8080}")
    private String wellnessServiceUrl;

    public GoalService(GoalRepository repository, RestTemplate restTemplate) {
        this.repository = repository;
        this.restTemplate = restTemplate;
    }

    public List<Goal> listAll() {
        return repository.findAll();
    }

    public List<Goal> findByStatus(String status) {
        try {
            GoalStatus goalStatus = GoalStatus.valueOf(status.toUpperCase(Locale.ROOT).replace("-", "_"));
            return repository.findByStatus(goalStatus);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status value: " + status);
        }
    }

    public List<Goal> findByCategory(String category) {
        return repository.findByCategoryIgnoreCase(category);
    }

    public Goal get(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new GoalNotFoundException(id));
    }

    public Goal create(GoalRequest request) {
        GoalStatus status;
        try {
            status = GoalStatus.valueOf(request.getStatus().toUpperCase(Locale.ROOT).replace("-", "_"));
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new IllegalArgumentException("Invalid status: " + request.getStatus());
        }

        Goal goal = new Goal(
                request.getTitle(),
                request.getDescription(),
                request.getTargetDate(),
                status,
                request.getCategory()
        );
        goal.setCreatedAt(Instant.now());
        goal.setUpdatedAt(Instant.now());
        return repository.save(goal);
    }

    public Goal update(String id, GoalRequest request) {
        Goal existing = get(id);
        existing.setTitle(request.getTitle());
        existing.setDescription(request.getDescription());
        existing.setTargetDate(request.getTargetDate());
        existing.setCategory(request.getCategory());

        if (request.getStatus() != null && !request.getStatus().isBlank()) {
            try {
                GoalStatus status = GoalStatus.valueOf(request.getStatus().toUpperCase(Locale.ROOT).replace("-", "_"));
                existing.setStatus(status);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid status: " + request.getStatus());
            }
        }

        existing.setUpdatedAt(Instant.now());
        return repository.save(existing);
    }

    public void delete(String id) {
        if (!repository.existsById(id)) {
            throw new GoalNotFoundException(id);
        }
        repository.deleteById(id);
    }

    public Goal markComplete(String id) {
        Goal goal = get(id);
        goal.setStatus(GoalStatus.COMPLETED);
        goal.setUpdatedAt(Instant.now());
        return repository.save(goal);
    }

    public List<ResourceResponse> suggestResourcesForGoal(String id) {
        Goal goal = get(id);
        String category = goal.getCategory();

        try {
            String url = wellnessServiceUrl + "/api/resources/category/" + category;
            ResourceResponse[] resources = restTemplate.getForObject(url, ResourceResponse[].class);
            return resources != null ? Arrays.asList(resources) : Collections.emptyList();
        } catch (RestClientException e) {
            System.err.println("Failed to fetch resources from wellness service: " + e.getMessage());
            return Collections.emptyList();
        }
    }
}