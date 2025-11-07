package com.gb.wellness.goal_tracking_service.goal.api;

import com.gb.wellness.goal_tracking_service.goal.domain.Goal;
import com.gb.wellness.goal_tracking_service.goal.service.GoalService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/goals")
public class GoalController {

    private final GoalService service;

    public GoalController(GoalService service) {
        this.service = service;
    }

    @GetMapping
    public List<GoalResponse> list(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String category
    ) {
        List<Goal> goals;

        if (status != null && !status.isBlank()) {
            goals = service.findByStatus(status);
        } else if (category != null && !category.isBlank()) {
            goals = service.findByCategory(category);
        } else {
            goals = service.listAll();
        }

        return goals.stream().map(this::toResponse).toList();
    }

    @GetMapping("/{id}")
    public GoalResponse get(@PathVariable String id) {
        return toResponse(service.get(id));
    }

    @PostMapping
    public ResponseEntity<GoalResponse> create(@Valid @RequestBody GoalRequest request) {
        Goal created = service.create(request);
        GoalResponse response = toResponse(created);
        return ResponseEntity
                .created(URI.create("/api/goals/" + response.getId()))
                .body(response);
    }

    @PutMapping("/{id}")
    public GoalResponse update(
            @PathVariable String id,
            @Valid @RequestBody GoalRequest request
    ) {
        return toResponse(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        service.delete(id);
    }

    @PatchMapping("/{id}/complete")
    public GoalResponse complete(@PathVariable String id) {
        return toResponse(service.markComplete(id));
    }

    @GetMapping("/{id}/suggested-resources")
    public List<ResourceResponse> suggestedResources(@PathVariable String id) {
        return service.suggestResourcesForGoal(id);
    }

    private GoalResponse toResponse(Goal goal) {
        GoalResponse resp = new GoalResponse();
        resp.setId(goal.getId());
        resp.setTitle(goal.getTitle());
        resp.setDescription(goal.getDescription());
        resp.setTargetDate(goal.getTargetDate());
        resp.setStatus(goal.getStatus());
        resp.setCategory(goal.getCategory());
        resp.setCreatedAt(goal.getCreatedAt());
        resp.setUpdatedAt(goal.getUpdatedAt());
        return resp;
    }
}
