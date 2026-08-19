package com.seam.repository;

import com.seam.entity.FieldExperience;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface FieldExperienceRepository extends JpaRepository<FieldExperience, Long> {
    List<FieldExperience> findByBranchIdAndModerationStatus(String branchId, FieldExperience.Moderation status);

    List<FieldExperience> findByModerationStatus(FieldExperience.Moderation status);

    boolean existsByBranchIdAndAuthorIdAndVisitDate(String branchId, String authorId, LocalDate visitDate);
}
