package ca.gbc.comp3095.event_service.resource.dto;

import jakarta.validation.constraints.NotBlank;

public class StudentRequest {
    @NotBlank(message = "Student ID is required")
    private String studentId;
    
    @NotBlank(message = "Student name is required")
    private String name;

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}