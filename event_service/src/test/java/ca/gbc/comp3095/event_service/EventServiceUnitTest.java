package ca.gbc.comp3095.event_service;

import ca.gbc.comp3095.event_service.resource.entity.Event;
import ca.gbc.comp3095.event_service.resource.repository.EventRepository;
import ca.gbc.comp3095.event_service.resource.service.EventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EventServiceUnitTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private EventService eventService;

    private Event testEvent;

    @BeforeEach
    void setUp() {
        testEvent = new Event();
        testEvent.setId(1L);
        testEvent.setTitle("Test Event");
        testEvent.setDescription("Test Description");
        testEvent.setDate(LocalDate.now().plusDays(7));
        testEvent.setLocation("Test Location");
        testEvent.setCapacity(10);
        testEvent.setCategory("wellness");
        testEvent.setRegisteredStudents(new HashSet<>());
    }

    @Test
    void shouldCreateEvent() {
        when(eventRepository.save(any(Event.class))).thenReturn(testEvent);

        Event created = eventService.create(testEvent);

        assertThat(created).isNotNull();
        assertThat(created.getTitle()).isEqualTo("Test Event");
        verify(eventRepository, times(1)).save(testEvent);
    }

    @Test
    void shouldGetEventById() {
        when(eventRepository.findById(1L)).thenReturn(Optional.of(testEvent));

        Event found = eventService.get(1L);

        assertThat(found).isNotNull();
        assertThat(found.getId()).isEqualTo(1L);
    }

    @Test
    void shouldThrowExceptionWhenEventNotFound() {
        when(eventRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> eventService.get(999L));
    }

    @Test
    void shouldRegisterStudent() {
        when(eventRepository.findById(1L)).thenReturn(Optional.of(testEvent));
        when(eventRepository.save(any(Event.class))).thenReturn(testEvent);

        Event registered = eventService.registerStudent(1L, "student123");

        assertThat(registered.getRegisteredStudents()).contains("student123");
        verify(eventRepository, times(1)).save(testEvent);
    }

    @Test
    void shouldThrowExceptionWhenRegisteringToFullEvent() {
        testEvent.setCapacity(1);
        testEvent.getRegisteredStudents().add("student1");
        when(eventRepository.findById(1L)).thenReturn(Optional.of(testEvent));

        assertThrows(RuntimeException.class, () -> 
            eventService.registerStudent(1L, "student2")
        );
    }

    @Test
    void shouldThrowExceptionWhenRegisteringDuplicateStudent() {
        testEvent.getRegisteredStudents().add("student123");
        when(eventRepository.findById(1L)).thenReturn(Optional.of(testEvent));

        assertThrows(RuntimeException.class, () -> 
            eventService.registerStudent(1L, "student123")
        );
    }

    @Test
    void shouldUnregisterStudent() {
        testEvent.getRegisteredStudents().add("student123");
        when(eventRepository.findById(1L)).thenReturn(Optional.of(testEvent));
        when(eventRepository.save(any(Event.class))).thenReturn(testEvent);

        Event unregistered = eventService.unregisterStudent(1L, "student123");

        assertThat(unregistered.getRegisteredStudents()).doesNotContain("student123");
    }

    @Test
    void shouldThrowExceptionWhenUnregisteringNonRegisteredStudent() {
        when(eventRepository.findById(1L)).thenReturn(Optional.of(testEvent));

        assertThrows(RuntimeException.class, () -> 
            eventService.unregisterStudent(1L, "student999")
        );
    }

    @Test
    void shouldCheckIfEventIsFull() {
        testEvent.setCapacity(2);
        testEvent.getRegisteredStudents().add("student1");
        testEvent.getRegisteredStudents().add("student2");

        assertThat(testEvent.isFull()).isTrue();
    }

    @Test
    void shouldCalculateAvailableSpots() {
        testEvent.setCapacity(10);
        testEvent.getRegisteredStudents().add("student1");
        testEvent.getRegisteredStudents().add("student2");

        assertThat(testEvent.getAvailableSpots()).isEqualTo(8);
    }
}
