package com.seam.repository;

import com.seam.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserAccountRepository extends JpaRepository<UserAccount, String> {
  boolean existsByEmail(String email);

  Optional<UserAccount> findByEmail(String email);

  /** 리프레시 토큰이 존재하는 사용자만 조회 (NULL 값 제외) */
  @Query("SELECT u FROM UserAccount u WHERE u.refreshToken IS NOT NULL")
  List<UserAccount> findUsersWithRefreshToken();
}