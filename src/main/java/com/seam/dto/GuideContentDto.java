package com.seam.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.OffsetDateTime;

public class GuideContentDto {
    @Schema(description = "콘텐츠 PK", example = "10")
    private Long contentId;

    @Schema(description = "콘텐츠 타입", example = "visa")
    private String contentType;

    @Schema(description = "버전 정보", example = "v1.2.0")
    private String version;

    @Schema(description = "오프라인 사용 가능 여부", example = "true")
    private boolean isOfflineAvailable;

    @Schema(description = "최종 수정 시각", example = "2026-08-18T10:30:00+09:00")
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
