package ca.gbc.comp3095.event_service.resource.dto;

import jakarta.validation.constraints.NotBlank;

public class StudentRequest {
    @NotBlank(message = "Student ID is required")
    private String studentId;

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
}