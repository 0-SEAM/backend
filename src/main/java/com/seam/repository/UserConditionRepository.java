package com.seam.repository;

import com.seam.entity.UserCondition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserConditionRepository extends JpaRepository<UserCondition, String> {
}
