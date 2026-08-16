package com.seam.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.OffsetDateTime;

@Entity
@Table(name = "field_experiences", uniqueConstraints = @UniqueConstraint(columnNames = {"branchId","authorId","visitDate"}))
public class FieldExperience {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long experienceId;

    private String branchId;

    private String authorId;

    private LocalDate visitDate;

    @Lob
    private String requiredDocs;

    @Lob
    private String visitResult;

    private Integer durationMinutes;

    @Enumerated(EnumType.STRING)
    private Moderation moderationStatus = Moderation.PENDING;

    private String failReason;

    private OffsetDateTime createdAt;

    public enum Moderation { PENDING, APPROVED, REJECTED }

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
    public Moderation getModerationStatus() { return moderationStatus; }
    public void setModerationStatus(Moderation moderationStatus) { this.moderationStatus = moderationStatus; }
    public String getFailReason() { return failReason; }
    public void setFailReason(String failReason) { this.failReason = failReason; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
}
