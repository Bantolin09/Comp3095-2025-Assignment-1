package com.gb.wellness.goal_tracking_service.goal.repository;

import com.gb.wellness.goal_tracking_service.goal.domain.Goal;
import com.gb.wellness.goal_tracking_service.goal.domain.GoalStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GoalRepository extends MongoRepository<Goal, String> {

    List<Goal> findByStatus(GoalStatus status);

    List<Goal> findByCategoryIgnoreCase(String category);
}
