package com.seam.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class FieldExperienceDto {
    @Schema(description = "현장 체험 PK", example = "1")
    private Long experienceId;

    @Schema(description = "브랜치 식별자", example = "branch-001", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    private String branchId;

    @Schema(description = "작성자 사용자 ID", example = "user-001", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    private String authorId;

    @Schema(description = "방문 일자", example = "2026-08-18", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    private LocalDate visitDate;

    @Schema(description = "필요 서류 설명", example = "여권 사본, 체류증")
    private String requiredDocs;

    @Schema(description = "방문 결과 메모", example = "현장 실습 완료")
    private String visitResult;

    @Schema(description = "소요 시간(분)", example = "180")
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
