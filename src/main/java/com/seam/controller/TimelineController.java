package com.seam.controller;

import com.seam.dto.TimelineTaskDto;
import com.seam.entity.TimelineTask;
import com.seam.service.TimelineService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/timelines")
public class TimelineController {
    private final TimelineService service;

    public TimelineController(TimelineService service) { this.service = service; }

    @GetMapping("/{userId}")
    public ResponseEntity<List<TimelineTask>> get(@PathVariable String userId, @RequestHeader("X-User-Id") String requester) {
        if (!userId.equals(requester)) return ResponseEntity.status(403).build();
        return ResponseEntity.ok(service.getTimeline(userId));
    }

    @PostMapping("/{userId}/tasks")
    public ResponseEntity<TimelineTask> create(@PathVariable String userId, @Valid @RequestBody TimelineTaskDto dto, @RequestHeader("X-User-Id") String requester) {
        if (!userId.equals(requester)) return ResponseEntity.status(403).build();
        TimelineTask t = new TimelineTask();
        t.setUserId(userId);
        t.setTaskType(dto.getTaskType());
        t.setPriority(dto.getPriority());
        t.setPrerequisiteTaskIds(dto.getPrerequisiteTaskIds());
        TimelineTask saved = service.createTask(t);
        return ResponseEntity.created(URI.create("/api/timelines/" + userId + "/tasks/" + saved.getTaskId())).body(saved);
    }

    @PostMapping("/{userId}/tasks/{taskId}/status")
    public ResponseEntity<TimelineTask> updateStatus(@PathVariable String userId, @PathVariable Long taskId, @RequestParam TimelineTask.Status status, @RequestHeader("X-User-Id") String requester) {
        if (!userId.equals(requester)) return ResponseEntity.status(403).build();
        TimelineTask updated = service.updateTaskStatus(taskId, status);
        return ResponseEntity.ok(updated);
    }
}
