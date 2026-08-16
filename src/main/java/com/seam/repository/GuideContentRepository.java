package com.seam.repository;

import com.seam.entity.GuideContent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GuideContentRepository extends JpaRepository<GuideContent, Long> {
    List<GuideContent> findByContentType(String contentType);
}
