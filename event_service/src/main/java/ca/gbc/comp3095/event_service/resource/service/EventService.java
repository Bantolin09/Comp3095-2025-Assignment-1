package ca.gbc.comp3095.event_service.resource.service;

import ca.gbc.comp3095.event_service.resource.entity.Event;
import ca.gbc.comp3095.event_service.resource.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestClientException;

import java.time.LocalDate;
import java.util.List;
import java.util.Arrays;
import java.util.Collections;

@Service
public class EventService {

    private final EventRepository repository;
    private final RestTemplate restTemplate;
    
    @Value("${wellness.resource.service.url:http://localhost:8080}")
    private String wellnessServiceUrl;

    @Autowired
    public EventService(EventRepository repository, RestTemplate restTemplate) {
        this.repository = repository;
        this.restTemplate = restTemplate;
    }

    public List<Event> listAll() {
        return repository.findAll();
    }

    public Event get(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + id));
    }

    public Event create(Event event) {
        return repository.save(event);
    }

    public Event update(Long id, Event event) {
        event.setId(id);
        return repository.save(event);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Event registerStudent(Long id, String studentId) {
        Event event = get(id);
        
        if (event.isFull()) {
            throw new RuntimeException("Event is full. Cannot register more students.");
        }
        
        if (event.getRegisteredStudents().contains(studentId)) {
            throw new RuntimeException("Student is already registered for this event.");
        }
        
        event.getRegisteredStudents().add(studentId);
        return repository.save(event);
    }

    public Event unregisterStudent(Long id, String studentId) {
        Event event = get(id);
        
        if (!event.getRegisteredStudents().contains(studentId)) {
            throw new RuntimeException("Student is not registered for this event.");
        }
        
        event.getRegisteredStudents().remove(studentId);
        return repository.save(event);
    }

    public List<Event> byDate(LocalDate date) {
        return repository.findByDate(date);
    }

    public List<Event> byLocation(String location) {
        return repository.findByLocationContainingIgnoreCase(location);
    }

    public List<Event> byCategory(String category) {
        return repository.findByCategoryContainingIgnoreCase(category);
    }
    
    // Inter-service communication - Get related wellness resources
    public List<Object> getRelatedResources(Long eventId) {
        Event event = get(eventId);
        try {
            String url = wellnessServiceUrl + "/api/resources/category/" + event.getCategory();
            Object[] resources = restTemplate.getForObject(url, Object[].class);
            return resources != null ? Arrays.asList(resources) : Collections.emptyList();
        } catch (RestClientException e) {
            // Log error and return empty list if wellness service is unavailable
            System.err.println("Failed to fetch resources from wellness service: " + e.getMessage());
            return Collections.emptyList();
        }
    }
    
    public List<Event> getEventsByStudent(String studentId) {
        return repository.findByRegisteredStudentsContaining(studentId);
    }
    
    public List<Event> getUpcomingEvents() {
        return repository.findByDateAfter(LocalDate.now());
    }
}