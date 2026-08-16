package com.seam.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.time.OffsetDateTime;

public class UserConditionDto {
    @NotNull
    private String userId;
    @NotNull
    private String visaStatus;
    @NotNull
    private LocalDate entryDate;
    private String workplaceLocation;
    private String residenceLocation;
    private LocalDate arcExpiryDate;
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
