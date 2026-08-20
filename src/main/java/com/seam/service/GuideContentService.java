package com.seam.service;

import com.seam.entity.GuideContent;
import com.seam.repository.GuideContentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GuideContentService {
    private final GuideContentRepository repo;

    public GuideContentService(GuideContentRepository repo) {
        this.repo = repo;
    }

    public List<GuideContent> listByType(String type) {
        if (type == null) {
            return repo.findAll();
        }
        return repo.findByContentType(type);
    }
}
