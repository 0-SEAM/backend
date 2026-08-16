package com.seam.controller;

import com.seam.entity.GuideContent;
import com.seam.service.GuideContentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/guides")
public class GuideContentController {
    private final GuideContentService service;

    public GuideContentController(GuideContentService service) { this.service = service; }

    @GetMapping("/sync")
    public ResponseEntity<List<GuideContent>> sync(@RequestParam(required = false) String type) {
        return ResponseEntity.ok(service.listByType(type));
    }
}
