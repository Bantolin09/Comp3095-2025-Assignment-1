package com.gb.wellness.goal_tracking_service.goal.exception;

public class GoalNotFoundException extends RuntimeException {
    public GoalNotFoundException(String id) {
        super("Goal not found with id: " + id);
    }
}
