package com.gb.wellness.goal_tracking_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.gb.wellness.goal_tracking_service")
public class GoalTrackingServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(GoalTrackingServiceApplication.class, args);
    }
}