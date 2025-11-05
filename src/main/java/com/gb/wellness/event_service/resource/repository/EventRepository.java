package com.gb.wellness.event_service.resource.repository;

import com.gb.wellness.event_service.resource.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByDate(LocalDate date);
    List<Event> findByLocationContainingIgnoreCase(String location);
    List<Event> findByCategoryContainingIgnoreCase(String category);
    List<Event> findByDateBetween(LocalDate startDate, LocalDate endDate);
    List<Event> findByCategory(String category);
    List<Event> findByDateAfter(LocalDate date);
    List<Event> findByRegisteredStudentsContaining(String studentId);
}