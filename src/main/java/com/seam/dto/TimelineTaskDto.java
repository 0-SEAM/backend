package com.seam.dto;

import com.seam.entity.TimelineTask;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

public class TimelineTaskDto {
    private Long taskId;
    @NotNull
    private String taskType;
    private Integer priority;
    private LocalDate recommendedDate;
    private TimelineTask.Status status;
    private List<Long> prerequisiteTaskIds;

    public Long getTaskId() { return taskId; }
    public void setTaskId(Long taskId) { this.taskId = taskId; }
    public String getTaskType() { return taskType; }
    public void setTaskType(String taskType) { this.taskType = taskType; }
    public Integer getPriority() { return priority; }
    public void setPriority(Integer priority) { this.priority = priority; }
    public LocalDate getRecommendedDate() { return recommendedDate; }
    public void setRecommendedDate(LocalDate recommendedDate) { this.recommendedDate = recommendedDate; }
    public TimelineTask.Status getStatus() { return status; }
    public void setStatus(TimelineTask.Status status) { this.status = status; }
    public List<Long> getPrerequisiteTaskIds() { return prerequisiteTaskIds; }
    public void setPrerequisiteTaskIds(List<Long> prerequisiteTaskIds) { this.prerequisiteTaskIds = prerequisiteTaskIds; }
}
