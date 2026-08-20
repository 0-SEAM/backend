package com.seam.service;

import com.seam.entity.TimelineTask;
import com.seam.repository.TimelineTaskRepository;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
public class TimelineService {
    private final TimelineTaskRepository repo;

    public TimelineService(TimelineTaskRepository repo) {
        this.repo = repo;
    }

    public List<TimelineTask> getTimeline(String userId) {
        return repo.findByUserIdOrderByPriorityAsc(userId);
    }

    @Transactional
    public TimelineTask updateTaskStatus(Long taskId, TimelineTask.Status status) {
        TimelineTask task = repo.findById(taskId).orElseThrow(() -> new NoSuchElementException("Task not found"));
        task.setStatus(status);
        if (status == TimelineTask.Status.DONE) {
            task.setCompletedAt(OffsetDateTime.now());
        }
        TimelineTask saved = repo.save(task);
        recalculateForUser(task.getUserId());
        return saved;
    }

    @Transactional
    public void recalculateForUser(String userId) {
        List<TimelineTask> tasks = repo.findByUserIdOrderByPriorityAsc(userId);
        Map<Long, TimelineTask> byId = new HashMap<Long, TimelineTask>();
        for (TimelineTask task : tasks) {
            byId.put(task.getTaskId(), task);
        }

        // example deterministic offsets per task type
        Map<String, Integer> baseOffset = Map.<String, Integer>of(
                "REGISTER_RESIDENCE", 7,
                "OPEN_BANK_ACCOUNT", 5,
                "GET_ARC", 14,
                "GET_SIM", 3
        );

        for (TimelineTask task : tasks) {
            LocalDate recommended = LocalDate.now();
            int offset = baseOffset.getOrDefault(task.getTaskType(), 1);
            recommended = recommended.plusDays(offset);
            if (task.getPrerequisiteTaskIds() != null && !task.getPrerequisiteTaskIds().isEmpty()) {
                LocalDate max = recommended;
                for (Long pid : task.getPrerequisiteTaskIds()) {
                    TimelineTask prerequisite = byId.get(pid);
                    if (prerequisite != null && prerequisite.getRecommendedDate() != null) {
                        LocalDate candidate = prerequisite.getRecommendedDate().plusDays(1);
                        if (candidate.isAfter(max)) {
                            max = candidate;
                        }
                    }
                }
                recommended = max;
            }
            task.setRecommendedDate(recommended);
            repo.save(task);
        }
    }

    @Transactional
    public TimelineTask createTask(TimelineTask task) {
        TimelineTask saved = repo.save(task);
        recalculateForUser(task.getUserId());
        return saved;
    }
}
