package com.seam.repository;

import com.seam.entity.TimelineTask;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TimelineTaskRepository extends JpaRepository<TimelineTask, Long> {
    List<TimelineTask> findByUserIdOrderByPriorityAsc(String userId);
}
