package com.seam.dto;

import com.seam.entity.TimelineTask;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

public class TimelineTaskDto {
    @Schema(description = "작업 PK", example = "11")
    private Long taskId;

    @Schema(description = "작업 유형", example = "document_check", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    private String taskType;

    @Schema(description = "우선순위 값", example = "1")
    private Integer priority;

    @Schema(description = "권장 실행 일자", example = "2026-08-20")
    private LocalDate recommendedDate;

    @Schema(description = "작업 상태", example = "TODO")
    private TimelineTask.Status status;

    @Schema(description = "선행 작업 ID 목록", example = "[1, 2]")
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
