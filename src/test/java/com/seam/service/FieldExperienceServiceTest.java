package com.seam.service;

import com.seam.entity.FieldExperience;
import com.seam.repository.FieldExperienceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FieldExperienceServiceTest {

    @Mock
    private FieldExperienceRepository repo;

    private FieldExperienceService service;

    @BeforeEach
    void setUp() {
        service = new FieldExperienceService(repo);
    }

    @Test
    void submit_shouldPersistExperienceAndSetDefaults() {
        FieldExperience exp = new FieldExperience();
        exp.setBranchId("branch-1");
        exp.setAuthorId("author-1");
        exp.setVisitDate(LocalDate.of(2026, 8, 20));
        exp.setRequiredDocs("Passport copy");
        exp.setVisitResult("Approved");
        exp.setDurationMinutes(120);

        when(repo.existsByBranchIdAndAuthorIdAndVisitDate("branch-1", "author-1", LocalDate.of(2026, 8, 20)))
                .thenReturn(false);
        when(repo.save(exp)).thenReturn(exp);

        FieldExperience saved = service.submit(exp);

        assertNotNull(saved.getCreatedAt());
        assertEquals(FieldExperience.Moderation.PENDING, saved.getModerationStatus());
        verify(repo).save(exp);
    }

    @Test
    void submit_shouldRejectDuplicateExperience() {
        FieldExperience exp = new FieldExperience();
        exp.setBranchId("branch-1");
        exp.setAuthorId("author-1");
        exp.setVisitDate(LocalDate.of(2026, 8, 20));

        when(repo.existsByBranchIdAndAuthorIdAndVisitDate("branch-1", "author-1", LocalDate.of(2026, 8, 20)))
                .thenReturn(true);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.submit(exp));

        assertEquals("Duplicate experience for same branch/author/visitDate", ex.getMessage());
        verify(repo, never()).save(any());
    }

    @Test
    void submit_shouldRejectPiiInRequiredDocsOrVisitResult() {
        FieldExperience exp = new FieldExperience();
        exp.setBranchId("branch-1");
        exp.setAuthorId("author-1");
        exp.setVisitDate(LocalDate.of(2026, 8, 20));
        exp.setRequiredDocs("주민번호 900101-1234567");

        when(repo.existsByBranchIdAndAuthorIdAndVisitDate("branch-1", "author-1", LocalDate.of(2026, 8, 20)))
                .thenReturn(false);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.submit(exp));

        assertEquals("Submission contains prohibited PII", ex.getMessage());
        verify(repo, never()).save(any());
    }

    @Test
    void listApprovedByBranch_shouldReturnApprovedExperiences() {
        FieldExperience one = new FieldExperience();
        when(repo.findByBranchIdAndModerationStatus("branch-1", FieldExperience.Moderation.APPROVED))
                .thenReturn(List.of(one));

        List<FieldExperience> result = service.listApprovedByBranch("branch-1");

        assertEquals(1, result.size());
        assertEquals(one, result.get(0));
    }

    @Test
    void listPending_shouldReturnPendingExperiences() {
        FieldExperience one = new FieldExperience();
        when(repo.findByModerationStatus(FieldExperience.Moderation.PENDING)).thenReturn(List.of(one));

        List<FieldExperience> result = service.listPending();

        assertEquals(List.of(one), result);
    }

    @Test
    void moderate_shouldUpdateStatusAndFailReason() {
        FieldExperience existing = new FieldExperience();
        existing.setExperienceId(5L);
        existing.setModerationStatus(FieldExperience.Moderation.PENDING);

        when(repo.findById(5L)).thenReturn(java.util.Optional.of(existing));
        when(repo.save(existing)).thenReturn(existing);

        FieldExperience updated = service.moderate(5L, FieldExperience.Moderation.REJECTED, "서류 누락");

        assertEquals(FieldExperience.Moderation.REJECTED, updated.getModerationStatus());
        assertEquals("서류 누락", updated.getFailReason());
        verify(repo).save(existing);
    }
}
