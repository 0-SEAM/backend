package com.seam.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class FieldExperienceDto {
    private Long experienceId;
    @NotBlank
    private String branchId;
    @NotBlank
    private String authorId;
    @NotNull
    private LocalDate visitDate;
    private String requiredDocs;
    private String visitResult;
    private Integer durationMinutes;

    public Long getExperienceId() { return experienceId; }
    public void setExperienceId(Long experienceId) { this.experienceId = experienceId; }
    public String getBranchId() { return branchId; }
    public void setBranchId(String branchId) { this.branchId = branchId; }
    public String getAuthorId() { return authorId; }
    public void setAuthorId(String authorId) { this.authorId = authorId; }
    public LocalDate getVisitDate() { return visitDate; }
    public void setVisitDate(LocalDate visitDate) { this.visitDate = visitDate; }
    public String getRequiredDocs() { return requiredDocs; }
    public void setRequiredDocs(String requiredDocs) { this.requiredDocs = requiredDocs; }
    public String getVisitResult() { return visitResult; }
    public void setVisitResult(String visitResult) { this.visitResult = visitResult; }
    public Integer getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(Integer durationMinutes) { this.durationMinutes = durationMinutes; }
}
