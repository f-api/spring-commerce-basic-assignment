package com.basic.schedule.repository;

import com.basic.schedule.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findAllByOrderByModifiedAt();

    List<Schedule> findAllByAuthorOrderByModifiedAt(String author);
}
