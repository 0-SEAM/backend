package com.seam.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.OffsetDateTime;

@Entity
@Table(name = "user_conditions")
public class UserCondition {
    @Id
    private String userId;

    private String visaStatus;

    private String visaType;

    private LocalDate entryDate;

    private LocalDate registrationAppliedDate;

    private String workplaceLocation;

    private String residenceLocation;

    private LocalDate arcExpiryDate;

    private OffsetDateTime updatedAt;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getVisaStatus() {
        return visaStatus;
    }

    public void setVisaStatus(String visaStatus) {
        this.visaStatus = visaStatus;
    }

    public String getVisaType() {
        return visaType;
    }

    public void setVisaType(String visaType) {
        this.visaType = visaType;
    }

    public LocalDate getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(LocalDate entryDate) {
        this.entryDate = entryDate;
    }

    public LocalDate getRegistrationAppliedDate() {
        return registrationAppliedDate;
    }

    public void setRegistrationAppliedDate(LocalDate registrationAppliedDate) {
        this.registrationAppliedDate = registrationAppliedDate;
    }

    public String getWorkplaceLocation() {
        return workplaceLocation;
    }

    public void setWorkplaceLocation(String workplaceLocation) {
        this.workplaceLocation = workplaceLocation;
    }

    public String getResidenceLocation() {
        return residenceLocation;
    }

    public void setResidenceLocation(String residenceLocation) {
        this.residenceLocation = residenceLocation;
    }

    public LocalDate getArcExpiryDate() {
        return arcExpiryDate;
    }

    public void setArcExpiryDate(LocalDate arcExpiryDate) {
        this.arcExpiryDate = arcExpiryDate;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
