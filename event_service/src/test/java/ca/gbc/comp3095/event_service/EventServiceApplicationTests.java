package ca.gbc.comp3095.event_service;

import ca.gbc.comp3095.event_service.resource.entity.Event;
import ca.gbc.comp3095.event_service.resource.repository.EventRepository;
import ca.gbc.comp3095.event_service.resource.service.EventService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Testcontainers
public class EventServiceApplicationTests {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("test_wellness_db")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private EventService eventService;

    @Autowired
    private EventRepository eventRepository;

    @Test
    void shouldCreateAndRetrieveEvent() {
        Event event = new Event();
        event.setTitle("Yoga Workshop");
        event.setDescription("Relaxing yoga session");
        event.setDate(LocalDate.now().plusDays(7));
        event.setLocation("Gym A");
        event.setCapacity(20);
        event.setCategory("mindfulness");

        Event saved = eventService.create(event);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getTitle()).isEqualTo("Yoga Workshop");
        
        Event retrieved = eventService.get(saved.getId());
        assertThat(retrieved.getTitle()).isEqualTo("Yoga Workshop");
    }

    @Test
    void shouldRegisterAndUnregisterStudent() {
        Event event = new Event();
        event.setTitle("Mental Health Seminar");
        event.setDescription("Understanding mental wellness");
        event.setDate(LocalDate.now().plusDays(5));
        event.setLocation("Room 101");
        event.setCapacity(2);
        event.setCategory("counseling");
        
        Event saved = eventService.create(event);
        String studentId = "student123";

        Event registered = eventService.registerStudent(saved.getId(), studentId);

        assertThat(registered.getRegisteredStudents()).contains(studentId);
        assertThat(registered.getAvailableSpots()).isEqualTo(1);

        Event unregistered = eventService.unregisterStudent(saved.getId(), studentId);

        assertThat(unregistered.getRegisteredStudents()).doesNotContain(studentId);
        assertThat(unregistered.getAvailableSpots()).isEqualTo(2);
    }

    @Test
    void shouldThrowExceptionWhenEventIsFull() {
        Event event = new Event();
        event.setTitle("Small Workshop");
        event.setDescription("Limited capacity event");
        event.setDate(LocalDate.now().plusDays(3));
        event.setLocation("Small Room");
        event.setCapacity(1);
        event.setCategory("mindfulness");
        
        Event saved = eventService.create(event);
        eventService.registerStudent(saved.getId(), "student1");

        assertThrows(RuntimeException.class, () -> {
            eventService.registerStudent(saved.getId(), "student2");
        });
    }

    @Test
    void shouldFilterEventsByDate() {
        LocalDate targetDate = LocalDate.now().plusDays(10);
        
        Event event1 = new Event();
        event1.setTitle("Event 1");
        event1.setDescription("First event");
        event1.setDate(targetDate);
        event1.setLocation("Location 1");
        event1.setCapacity(10);
        event1.setCategory("wellness");

        eventService.create(event1);

        var eventsByDate = eventService.byDate(targetDate);

        assertThat(eventsByDate).hasSize(1);
        assertThat(eventsByDate.get(0).getTitle()).isEqualTo("Event 1");
    }

    @Test
    void shouldFilterEventsByLocation() {
        Event event1 = new Event();
        event1.setTitle("Gym Event");
        event1.setDescription("Fitness event");
        event1.setDate(LocalDate.now().plusDays(5));
        event1.setLocation("Main Gym");
        event1.setCapacity(25);
        event1.setCategory("fitness");

        eventService.create(event1);

        var gymEvents = eventService.byLocation("Gym");

        assertThat(gymEvents).hasSize(1);
        assertThat(gymEvents.get(0).getTitle()).isEqualTo("Gym Event");
    }
}