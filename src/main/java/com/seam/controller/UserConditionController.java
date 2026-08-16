package com.seam.controller;

import com.seam.entity.UserCondition;
import com.seam.dto.UserConditionDto;
import com.seam.service.UserConditionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/conditions")
public class UserConditionController {
    private final UserConditionService service;

    public UserConditionController(UserConditionService service) { this.service = service; }

    @PostMapping
    public ResponseEntity<UserCondition> create(@Valid @RequestBody UserConditionDto dto, @RequestHeader("X-User-Id") String requester) {
        UserCondition u = new UserCondition();
        u.setUserId(dto.getUserId());
        u.setVisaStatus(dto.getVisaStatus());
        u.setEntryDate(dto.getEntryDate());
        u.setWorkplaceLocation(dto.getWorkplaceLocation());
        u.setResidenceLocation(dto.getResidenceLocation());
        u.setArcExpiryDate(dto.getArcExpiryDate());
        UserCondition saved = service.upsert(u, requester);
        return ResponseEntity.created(URI.create("/api/conditions/" + saved.getUserId())).body(saved);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserCondition> get(@PathVariable String userId, @RequestHeader("X-User-Id") String requester) {
        return service.findByUserId(userId, requester).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserCondition> update(@PathVariable String userId, @Valid @RequestBody UserConditionDto dto, @RequestHeader("X-User-Id") String requester) {
        if (!userId.equals(dto.getUserId())) return ResponseEntity.badRequest().build();
        UserCondition u = new UserCondition();
        u.setUserId(dto.getUserId());
        u.setVisaStatus(dto.getVisaStatus());
        u.setEntryDate(dto.getEntryDate());
        u.setWorkplaceLocation(dto.getWorkplaceLocation());
        u.setResidenceLocation(dto.getResidenceLocation());
        u.setArcExpiryDate(dto.getArcExpiryDate());
        return ResponseEntity.ok(service.upsert(u, requester));
    }
}
