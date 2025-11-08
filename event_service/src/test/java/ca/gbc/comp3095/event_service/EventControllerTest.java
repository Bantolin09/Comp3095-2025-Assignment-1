package ca.gbc.comp3095.event_service;

import ca.gbc.comp3095.event_service.resource.controller.EventController;
import ca.gbc.comp3095.event_service.resource.entity.Event;
import ca.gbc.comp3095.event_service.resource.service.EventService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EventController.class)
public class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EventService eventService;

    @MockBean
    private RestTemplate restTemplate;

    @Test
    void shouldGetAllEvents() throws Exception {
        Event event = createTestEvent();
        when(eventService.listAll()).thenReturn(Arrays.asList(event));

        mockMvc.perform(get("/api/events"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Test Event"));
    }

    @Test
    void shouldGetEventById() throws Exception {
        Event event = createTestEvent();
        when(eventService.get(1L)).thenReturn(event);

        mockMvc.perform(get("/api/events/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Event"));
    }

    @Test
    void shouldCreateEvent() throws Exception {
        Event event = createTestEvent();
        when(eventService.create(any(Event.class))).thenReturn(event);

        String requestBody = """
                {
                    "title": "Test Event",
                    "description": "Test Description",
                    "date": "2025-12-01",
                    "location": "Test Location",
                    "capacity": 50,
                    "category": "wellness"
                }
                """;

        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Event"));
    }

    @Test
    void shouldRegisterStudent() throws Exception {
        Event event = createTestEvent();
        event.getRegisteredStudents().add("student123");
        when(eventService.registerStudent(1L, "student123")).thenReturn(event);

        String requestBody = "{\"studentId\": \"student123\"}";

        mockMvc.perform(post("/api/events/1/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.registeredStudents[0]").value("student123"));
    }

    @Test
    void shouldFilterByDate() throws Exception {
        Event event = createTestEvent();
        when(eventService.byDate(any(LocalDate.class))).thenReturn(Arrays.asList(event));

        mockMvc.perform(get("/api/events/date/2025-12-01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Test Event"));
    }

    private Event createTestEvent() {
        Event event = new Event();
        event.setId(1L);
        event.setTitle("Test Event");
        event.setDescription("Test Description");
        event.setDate(LocalDate.of(2025, 12, 1));
        event.setLocation("Test Location");
        event.setCapacity(50);
        event.setCategory("wellness");
        event.setRegisteredStudents(new HashSet<>());
        return event;
    }
}
