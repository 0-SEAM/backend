package com.seam.service;

import com.seam.dto.AuthResponse;
import com.seam.dto.LoginRequest;
import com.seam.dto.RefreshTokenRequest;
import com.seam.dto.SignupRequest;
import com.seam.entity.UserAccount;
import com.seam.repository.UserAccountRepository;
import com.seam.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
public class AuthService {
    private final UserAccountRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserAccountRepository repository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Transactional
    public AuthResponse signup(SignupRequest request) {
        String email = request.getEmail().trim().toLowerCase();
        if (repository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email is already registered");
        }

        UserAccount user = new UserAccount();
        user.setUserId(UUID.randomUUID().toString());
        user.setName(request.getName().trim());
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRole(UserAccount.Role.USER);
        return issueTokens(repository.save(user));
    }

    @Transactional
    public AuthResponse login(LoginRequest request) {
        UserAccount user = repository.findByEmail(request.getEmail().trim().toLowerCase())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));
        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid email or password");
        }
        return issueTokens(user);
    }

    @Transactional
    public AuthResponse refresh(RefreshTokenRequest request) {
        String userId;
        try {
            if (!jwtService.isRefreshToken(request.getRefreshToken())) {
                throw new IllegalArgumentException("Invalid refresh token");
            }
            userId = jwtService.getUserId(request.getRefreshToken());
        } catch (RuntimeException ex) {
            throw new IllegalArgumentException("Invalid refresh token", ex);
        }

        UserAccount user = repository.findById(userId)
            .filter(account -> account.getRefreshToken() != null
                && passwordEncoder.matches(request.getRefreshToken(), account.getRefreshToken()))
                .filter(account -> account.getRefreshTokenExpiresAt() != null && account.getRefreshTokenExpiresAt().isAfter(OffsetDateTime.now()))
                .orElseThrow(() -> new IllegalArgumentException("Refresh token is expired or revoked"));
        return issueTokens(user);
    }

    @Transactional
    public void logout(RefreshTokenRequest request) {
        repository.findAll().stream()
            .filter(user -> user.getRefreshToken() != null
                && passwordEncoder.matches(request.getRefreshToken(), user.getRefreshToken()))
                .findFirst()
                .ifPresent(user -> {
                    user.setRefreshToken(null);
                    user.setRefreshTokenExpiresAt(null);
                    repository.save(user);
                });
    }

    private AuthResponse issueTokens(UserAccount user) {
        String accessToken = jwtService.createAccessToken(user);
        String refreshToken = jwtService.createRefreshToken(user);
        user.setRefreshToken(passwordEncoder.encode(refreshToken));
        user.setRefreshTokenExpiresAt(OffsetDateTime.now().plusNanos(jwtService.refreshExpirationMs() * 1_000_000));
        repository.save(user);
        return new AuthResponse(user.getUserId(), user.getName(), user.getEmail(), user.getRole().name(), accessToken, refreshToken);
    }
}
