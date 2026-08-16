package com.seam.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

@Entity
@Table(name = "timeline_tasks")
public class TimelineTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long taskId;

    private String userId;

    private String taskType;

    private Integer priority;

    private LocalDate recommendedDate;

    @Enumerated(EnumType.STRING)
    private Status status = Status.TODO;

    private OffsetDateTime completedAt;

    @ElementCollection
    private List<Long> prerequisiteTaskIds;

    public enum Status { TODO, IN_PROGRESS, DONE }

    public Long getTaskId() { return taskId; }
    public void setTaskId(Long taskId) { this.taskId = taskId; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getTaskType() { return taskType; }
    public void setTaskType(String taskType) { this.taskType = taskType; }
    public Integer getPriority() { return priority; }
    public void setPriority(Integer priority) { this.priority = priority; }
    public LocalDate getRecommendedDate() { return recommendedDate; }
    public void setRecommendedDate(LocalDate recommendedDate) { this.recommendedDate = recommendedDate; }
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
    public OffsetDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(OffsetDateTime completedAt) { this.completedAt = completedAt; }
    public List<Long> getPrerequisiteTaskIds() { return prerequisiteTaskIds; }
    public void setPrerequisiteTaskIds(List<Long> prerequisiteTaskIds) { this.prerequisiteTaskIds = prerequisiteTaskIds; }
}
