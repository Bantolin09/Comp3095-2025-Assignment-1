package com.gb.wellness.event_service.resource.dto;

import java.time.LocalDate;
import java.util.Set;

public class EventResponse {
    private Long id;
    private String title;
    private String description;
    private LocalDate date;
    private String location;
    private Integer capacity;
    private String category;
    private Set<String> registeredStudents;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    
    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    public Set<String> getRegisteredStudents() { return registeredStudents; }
    public void setRegisteredStudents(Set<String> registeredStudents) { this.registeredStudents = registeredStudents; }
}
