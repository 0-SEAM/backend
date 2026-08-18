package com.seam.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.OffsetDateTime;

public class UserConditionDto {
    @Schema(description = "사용자 ID", example = "user-001", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    private String userId;

    @Schema(description = "비자 상태", example = "WORKING", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    private String visaStatus;

    @Schema(description = "입국 일자", example = "2024-01-15", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    private LocalDate entryDate;

    @Schema(description = "근무지 위치", example = "Seoul")
    private String workplaceLocation;

    @Schema(description = "거주지 위치", example = "Busan")
    private String residenceLocation;

    @Schema(description = "ARC 만료일", example = "2027-12-31")
    private LocalDate arcExpiryDate;

    @Schema(description = "최종 수정 시각", example = "2026-08-18T09:00:00+09:00")
    private OffsetDateTime updatedAt;

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getVisaStatus() { return visaStatus; }
    public void setVisaStatus(String visaStatus) { this.visaStatus = visaStatus; }
    public LocalDate getEntryDate() { return entryDate; }
    public void setEntryDate(LocalDate entryDate) { this.entryDate = entryDate; }
    public String getWorkplaceLocation() { return workplaceLocation; }
    public void setWorkplaceLocation(String workplaceLocation) { this.workplaceLocation = workplaceLocation; }
    public String getResidenceLocation() { return residenceLocation; }
    public void setResidenceLocation(String residenceLocation) { this.residenceLocation = residenceLocation; }
    public LocalDate getArcExpiryDate() { return arcExpiryDate; }
    public void setArcExpiryDate(LocalDate arcExpiryDate) { this.arcExpiryDate = arcExpiryDate; }
    public OffsetDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(OffsetDateTime updatedAt) { this.updatedAt = updatedAt; }
}
