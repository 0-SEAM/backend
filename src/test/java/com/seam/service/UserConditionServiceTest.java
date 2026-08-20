package com.seam.service;

import com.seam.entity.UserCondition;
import com.seam.repository.UserConditionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserConditionServiceTest {

    @Mock
    private UserConditionRepository repo;

    private UserConditionService service;

    @BeforeEach
    void setUp() {
        service = new UserConditionService(repo);
    }

    @Test
    void upsert_shouldAllowOwnerToUpdateCondition() {
        UserCondition cond = new UserCondition();
        cond.setUserId("user-1");
        cond.setVisaStatus("D-2");
        when(repo.save(cond)).thenReturn(cond);

        UserCondition saved = service.upsert(cond, "user-1");

        assertNotNull(saved);
        assertNotNull(saved.getUpdatedAt());
        assertEquals("user-1", saved.getUserId());
        assertEquals("D-2", saved.getVisaStatus());
        verify(repo).save(cond);
    }

    @Test
    void upsert_shouldRejectUpdatingAnotherUsersData() {
        UserCondition cond = new UserCondition();
        cond.setUserId("user-2");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.upsert(cond, "user-1"));

        assertEquals("Can only modify own data", ex.getMessage());
    }

    @Test
    void findByUserId_shouldReturnSavedConditionForOwner() {
        UserCondition cond = new UserCondition();
        cond.setUserId("user-1");
        when(repo.findById("user-1")).thenReturn(Optional.of(cond));

        Optional<UserCondition> result = service.findByUserId("user-1", "user-1");

        assertTrue(result.isPresent());
        assertEquals("user-1", result.get().getUserId());
        verify(repo).findById("user-1");
    }

    @Test
    void findByUserId_shouldRejectViewingAnotherUsersData() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.findByUserId("user-1", "user-2"));

        assertEquals("Can only view own data", ex.getMessage());
    }
}
