package com.gb.wellness.goal_tracking_service.goal.repository;

import com.gb.wellness.goal_tracking_service.goal.domain.Goal;
import com.gb.wellness.goal_tracking_service.goal.domain.GoalStatus;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface GoalRepository extends MongoRepository<Goal, String> {

    List<Goal> findByStatus(GoalStatus status);

    List<Goal> findByCategoryIgnoreCase(String category);
}
