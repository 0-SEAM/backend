package com.seam.entity;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "guide_contents")
public class GuideContent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long contentId;

    private String contentType;

    private String version;

    private boolean isOfflineAvailable;

    private OffsetDateTime lastUpdatedAt;

    public Long getContentId() { return contentId; }
    public void setContentId(Long contentId) { this.contentId = contentId; }
    public String getContentType() { return contentType; }
    public void setContentType(String contentType) { this.contentType = contentType; }
    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }
    public boolean isOfflineAvailable() { return isOfflineAvailable; }
    public void setOfflineAvailable(boolean offlineAvailable) { isOfflineAvailable = offlineAvailable; }
    public OffsetDateTime getLastUpdatedAt() { return lastUpdatedAt; }
    public void setLastUpdatedAt(OffsetDateTime lastUpdatedAt) { this.lastUpdatedAt = lastUpdatedAt; }
}
