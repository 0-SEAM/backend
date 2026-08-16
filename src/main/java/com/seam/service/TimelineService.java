package com.seam.service;

import com.seam.entity.TimelineTask;
import com.seam.repository.TimelineTaskRepository;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;

@Service
public class TimelineService {
    private final TimelineTaskRepository repo;

    public TimelineService(TimelineTaskRepository repo) { this.repo = repo; }

    public List<TimelineTask> getTimeline(String userId) {
        return repo.findByUserIdOrderByPriorityAsc(userId);
    }

    @Transactional
    public TimelineTask updateTaskStatus(Long taskId, TimelineTask.Status status) {
        TimelineTask t = repo.findById(taskId).orElseThrow(() -> new NoSuchElementException("Task not found"));
        t.setStatus(status);
        if (status == TimelineTask.Status.DONE) t.setCompletedAt(java.time.OffsetDateTime.now());
        TimelineTask saved = repo.save(t);
        recalculateForUser(t.getUserId());
        return saved;
    }

    @Transactional
    public void recalculateForUser(String userId) {
        List<TimelineTask> tasks = repo.findByUserIdOrderByPriorityAsc(userId);
        Map<Long, TimelineTask> byId = new HashMap<>();
        for (TimelineTask t : tasks) byId.put(t.getTaskId(), t);

        // example deterministic offsets per task type
        Map<String, Integer> baseOffset = Map.of(
                "REGISTER_RESIDENCE", 7,
                "OPEN_BANK_ACCOUNT", 5,
                "GET_ARC", 14,
                "GET_SIM", 3
        );

        for (TimelineTask t : tasks) {
            LocalDate recommended = LocalDate.now();
            int offset = baseOffset.getOrDefault(t.getTaskType(), 1);
            recommended = recommended.plusDays(offset);
            if (t.getPrerequisiteTaskIds() != null && !t.getPrerequisiteTaskIds().isEmpty()) {
                LocalDate max = recommended;
                for (Long pid : t.getPrerequisiteTaskIds()) {
                    TimelineTask p = byId.get(pid);
                    if (p != null && p.getRecommendedDate() != null) {
                        LocalDate candidate = p.getRecommendedDate().plusDays(1);
                        if (candidate.isAfter(max)) max = candidate;
                    }
                }
                recommended = max;
            }
            t.setRecommendedDate(recommended);
            repo.save(t);
        }
    }

    @Transactional
    public TimelineTask createTask(TimelineTask t) {
        TimelineTask saved = repo.save(t);
        recalculateForUser(t.getUserId());
        return saved;
    }
}
