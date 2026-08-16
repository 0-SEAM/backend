package com.seam.controller;

import com.seam.dto.FieldExperienceDto;
import com.seam.entity.FieldExperience;
import com.seam.service.FieldExperienceService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/experiences")
public class FieldExperienceController {
    private final FieldExperienceService service;

    public FieldExperienceController(FieldExperienceService service) { this.service = service; }

    @PostMapping
    public ResponseEntity<FieldExperience> submit(@Valid @RequestBody FieldExperienceDto dto) {
        FieldExperience e = new FieldExperience();
        e.setBranchId(dto.getBranchId());
        e.setAuthorId(dto.getAuthorId());
        e.setVisitDate(dto.getVisitDate());
        e.setRequiredDocs(dto.getRequiredDocs());
        e.setVisitResult(dto.getVisitResult());
        e.setDurationMinutes(dto.getDurationMinutes());
        FieldExperience saved = service.submit(e);
        return ResponseEntity.created(URI.create("/api/experiences/" + saved.getExperienceId())).body(saved);
    }

    @GetMapping
    public ResponseEntity<List<FieldExperience>> listApproved(@RequestParam String branchId) {
        return ResponseEntity.ok(service.listApprovedByBranch(branchId));
    }

    @PostMapping("/{id}/moderate")
    public ResponseEntity<FieldExperience> moderate(@PathVariable Long id, @RequestParam FieldExperience.Moderation moderation, @RequestParam(required = false) String failReason) {
        FieldExperience updated = service.moderate(id, moderation, failReason);
        return ResponseEntity.ok(updated);
    }
}
