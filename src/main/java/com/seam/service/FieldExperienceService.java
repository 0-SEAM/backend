package com.seam.service;

import com.seam.entity.FieldExperience;
import com.seam.repository.FieldExperienceRepository;
import com.seam.util.PiiUtils;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.time.OffsetDateTime;
import java.util.List;

@Service
public class FieldExperienceService {
    private final FieldExperienceRepository repo;

    public FieldExperienceService(FieldExperienceRepository repo) { this.repo = repo; }

    @Transactional
    public FieldExperience submit(FieldExperience exp) {
        // duplicate prevention
        if (repo.existsByBranchIdAndAuthorIdAndVisitDate(exp.getBranchId(), exp.getAuthorId(), exp.getVisitDate())) {
            throw new IllegalArgumentException("Duplicate experience for same branch/author/visitDate");
        }

        // PII validation: reject or mask
        if (PiiUtils.containsPii(exp.getRequiredDocs()) || PiiUtils.containsPii(exp.getVisitResult())) {
            // choose rejection to be strict
            throw new IllegalArgumentException("Submission contains prohibited PII");
        }

        exp.setCreatedAt(OffsetDateTime.now());
        exp.setModerationStatus(FieldExperience.Moderation.PENDING);
        return repo.save(exp);
    }

    public List<FieldExperience> listApprovedByBranch(String branchId) {
        return repo.findByBranchIdAndModerationStatus(branchId, FieldExperience.Moderation.APPROVED);
    }

    @Transactional
    public FieldExperience moderate(Long experienceId, FieldExperience.Moderation moderation, String failReason) {
        FieldExperience e = repo.findById(experienceId).orElseThrow(() -> new IllegalArgumentException("Not found"));
        e.setModerationStatus(moderation);
        e.setFailReason(failReason);
        return repo.save(e);
    }
}
