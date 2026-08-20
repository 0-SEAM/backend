package com.seam.controller;

import com.seam.entity.UserCondition;
import com.seam.dto.UserConditionDto;
import com.seam.service.UserConditionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/conditions")
@Tag(name = "User Condition", description = "사용자 비자/거주/근무 조건 관리 API")
public class UserConditionController {
        private final UserConditionService service;

        public UserConditionController(UserConditionService service) {
                this.service = service;
        }

        @PostMapping
        @Operation(summary = "사용자 조건 생성 또는 갱신", description = "사용자의 비자, 체류, 근무 조건 정보를 생성 또는 최신화합니다.")
        @ApiResponses({
                        @ApiResponse(responseCode = "201", description = "생성 성공", content = @Content(schema = @Schema(implementation = UserCondition.class))),
                        @ApiResponse(responseCode = "400", description = "요청 값 검증 실패"),
                        @ApiResponse(responseCode = "403", description = "권한 없음")
        })
        public ResponseEntity<UserCondition> create(
                        @Valid @RequestBody UserConditionDto dto,
                        @Parameter(name = "X-User-Id", description = "요청자 사용자 ID", required = true, example = "user-001") @RequestHeader("X-User-Id") String requester) {
                UserCondition u = new UserCondition();
                u.setUserId(dto.getUserId());
                u.setVisaStatus(dto.getVisaStatus());
                u.setEntryDate(dto.getEntryDate());
                u.setRegistrationAppliedDate(dto.getRegistrationAppliedDate());
                u.setWorkplaceLocation(dto.getWorkplaceLocation());
                u.setResidenceLocation(dto.getResidenceLocation());
                u.setArcExpiryDate(dto.getArcExpiryDate());
                UserCondition saved = service.upsert(u, requester);
                return ResponseEntity.created(URI.create("/api/conditions/" + saved.getUserId())).body(saved);
        }

        @GetMapping("/{userId}")
        @Operation(summary = "사용자 조건 조회", description = "특정 사용자의 조건 정보를 조회합니다.")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = UserCondition.class))),
                        @ApiResponse(responseCode = "404", description = "해당 사용자 조건 없음")
        })
        public ResponseEntity<UserCondition> get(
                        @Parameter(description = "조회 대상 사용자 ID", example = "user-001", required = true) @PathVariable String userId,
                        @Parameter(name = "X-User-Id", description = "요청자 사용자 ID", required = true, example = "user-001") @RequestHeader("X-User-Id") String requester) {
                return service.findByUserId(userId, requester).map(ResponseEntity::ok)
                                .orElseGet(() -> ResponseEntity.notFound().build());
        }

        @PutMapping("/{userId}")
        @Operation(summary = "사용자 조건 수정", description = "기존 사용자 조건을 전체 갱신합니다.")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "수정 성공", content = @Content(schema = @Schema(implementation = UserCondition.class))),
                        @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
                        @ApiResponse(responseCode = "403", description = "권한 없음")
        })
        public ResponseEntity<UserCondition> update(
                        @Parameter(description = "수정 대상 사용자 ID", example = "user-001", required = true) @PathVariable String userId,
                        @Valid @RequestBody UserConditionDto dto,
                        @Parameter(name = "X-User-Id", description = "요청자 사용자 ID", required = true, example = "user-001") @RequestHeader("X-User-Id") String requester) {
                if (!userId.equals(dto.getUserId()))
                        return ResponseEntity.badRequest().build();
                UserCondition u = new UserCondition();
                u.setUserId(dto.getUserId());
                u.setVisaStatus(dto.getVisaStatus());
                u.setEntryDate(dto.getEntryDate());
                u.setRegistrationAppliedDate(dto.getRegistrationAppliedDate());
                u.setWorkplaceLocation(dto.getWorkplaceLocation());
                u.setResidenceLocation(dto.getResidenceLocation());
                u.setArcExpiryDate(dto.getArcExpiryDate());
                return ResponseEntity.ok(service.upsert(u, requester));
        }
}
