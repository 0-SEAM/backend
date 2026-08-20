package com.seam.service;

import com.seam.entity.UserCondition;
import com.seam.repository.UserConditionRepository;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Optional;

@Service
public class UserConditionService {
    private final UserConditionRepository repo;

    public UserConditionService(UserConditionRepository repo) {
        this.repo = repo;
    }

    public UserCondition upsert(UserCondition cond, String requesterUserId) {
        if (!requesterUserId.equals(cond.getUserId())) {
            throw new IllegalArgumentException("Can only modify own data");
        }
        cond.setUpdatedAt(OffsetDateTime.now());
        return repo.save(cond);
    }

    public Optional<UserCondition> findByUserId(String userId, String requesterUserId) {
        if (!requesterUserId.equals(userId)) {
            throw new IllegalArgumentException("Can only view own data");
        }
        return repo.findById(userId);
    }
}
