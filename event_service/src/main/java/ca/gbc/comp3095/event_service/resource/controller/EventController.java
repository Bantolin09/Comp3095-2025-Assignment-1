package ca.gbc.comp3095.event_service.resource.controller;

import ca.gbc.comp3095.event_service.resource.dto.EventRequest;
import ca.gbc.comp3095.event_service.resource.dto.EventResponse;
import ca.gbc.comp3095.event_service.resource.dto.StudentRequest;
import ca.gbc.comp3095.event_service.resource.entity.Event;
import ca.gbc.comp3095.event_service.resource.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import jakarta.validation.Valid;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.validation.FieldError;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService service;
    private final RestTemplate restTemplate;

    @GetMapping
    public List<EventResponse> listAll() {
        return service.listAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public EventResponse get(@PathVariable Long id) {
        return toResponse(service.get(id));
    }

    @PostMapping
    public EventResponse create(@Valid @RequestBody EventRequest req) {
        Event event = new Event();
        // Set fields from request
        event.setTitle(req.getTitle());
        event.setDescription(req.getDescription());
        event.setDate(req.getDate());
        event.setLocation(req.getLocation());
        event.setCapacity(req.getCapacity());
        event.setCategory(req.getCategory());
        return toResponse(service.create(event));
    }

    @PutMapping("/{id}")
    public EventResponse update(@PathVariable Long id, @Valid @RequestBody EventRequest req) {
        Event event = service.get(id);
        event.setTitle(req.getTitle());
        event.setDescription(req.getDescription());
        event.setDate(req.getDate());
        event.setLocation(req.getLocation());
        event.setCapacity(req.getCapacity());
        event.setCategory(req.getCategory());
        return toResponse(service.update(id, event));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    // Register/unregister endpoints
    @PostMapping("/{id}/register")
    public EventResponse register(@PathVariable Long id, @Valid @RequestBody StudentRequest request) {
        return toResponse(service.registerStudent(id, request.getStudentId()));
    }

    @PostMapping("/{id}/unregister")
    public EventResponse unregister(@PathVariable Long id, @Valid @RequestBody StudentRequest request) {
        return toResponse(service.unregisterStudent(id, request.getStudentId()));
    }

    // Filter endpoints
    @GetMapping("/date/{date}")
    public List<EventResponse> byDate(@PathVariable LocalDate date) {
        return service.byDate(date).stream().map(this::toResponse).collect(Collectors.toList());
    }
    @GetMapping("/location/{location}")
    public List<EventResponse> byLocation(@PathVariable String location) {
        return service.byLocation(location).stream().map(this::toResponse).collect(Collectors.toList());
    }
    @GetMapping("/category/{category}")
    public List<EventResponse> byCategory(@PathVariable String category) {
        return service.byCategory(category).stream().map(this::toResponse).collect(Collectors.toList());
    }

    // Inter-service resource fetch: Get related wellness resources
    @GetMapping("/{id}/resources")
    public List<Object> getResourcesForEvent(@PathVariable Long id) {
        return service.getRelatedResources(id);
    }

    // Additional endpoints for assignment requirements
    @GetMapping("/student/{studentId}")
    public List<EventResponse> getEventsByStudent(@PathVariable String studentId) {
        return service.getEventsByStudent(studentId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/upcoming")
    public List<EventResponse> getUpcomingEvents() {
        return service.getUpcomingEvents().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleNotFound(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    private EventResponse toResponse(Event event) {
        EventResponse resp = new EventResponse();
        resp.setId(event.getId());
        resp.setTitle(event.getTitle());
        resp.setDescription(event.getDescription());
        resp.setDate(event.getDate());
        resp.setLocation(event.getLocation());
        resp.setCapacity(event.getCapacity());
        resp.setCategory(event.getCategory());
        resp.setRegisteredStudents(event.getRegisteredStudents());
        return resp;
    }
}
