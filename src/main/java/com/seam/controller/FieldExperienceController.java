package com.seam.controller;

import com.seam.dto.FieldExperienceDto;
import com.seam.entity.FieldExperience;
import com.seam.service.FieldExperienceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/experiences")
@Tag(name = "Field Experience", description = "현장 체험 신청 및 승인 관리 API")
public class FieldExperienceController {
    private final FieldExperienceService service;

    public FieldExperienceController(FieldExperienceService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "현장 체험 신청 등록", description = "브랜치 기준 현장 체험 정보를 등록합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "생성 성공", content = @Content(schema = @Schema(implementation = FieldExperience.class))),
            @ApiResponse(responseCode = "400", description = "요청 값 검증 실패"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResponseEntity<FieldExperience> submit(@Valid @RequestBody FieldExperienceDto dto) {
        FieldExperience e = new FieldExperience();
        e.setBranchId(dto.getBranchId());
        e.setAuthorId(dto.getAuthorId());
        e.setVisitDate(dto.getVisitDate());
        e.setRequiredDocs(dto.getRequiredDocs());
        e.setVisitResult(dto.getVisitResult());
        e.setDurationMinutes(dto.getDurationMinutes());
        FieldExperience saved = service.submit(e);
        return ResponseEntity.created(URI.create("/api/experiences/" + saved.getExperienceId())).body(saved);
    }

    @GetMapping
    @Operation(summary = "브랜치별 승인된 현장 체험 조회", description = "브랜치 ID로 승인된 현장 체험 목록을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = FieldExperience.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    public ResponseEntity<List<FieldExperience>> listApproved(
            @Parameter(description = "조회 대상 브랜치 ID", example = "branch-001", required = true) @RequestParam String branchId) {
        return ResponseEntity.ok(service.listApprovedByBranch(branchId));
    }

    @GetMapping("/pending")
    @Operation(summary = "검토 대기 현장 체험 조회", description = "검토가 필요한 현장 체험 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = FieldExperience.class)))
    public ResponseEntity<List<FieldExperience>> listPending() {
        return ResponseEntity.ok(service.listPending());
    }

    @PostMapping("/{id}/moderate")
    @Operation(summary = "현장 체험 승인/반려 처리", description = "관리자가 신청 상태를 승인 또는 반려로 변경합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "상태 변경 성공", content = @Content(schema = @Schema(implementation = FieldExperience.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 상태 값 또는 입력값"),
            @ApiResponse(responseCode = "404", description = "대상 데이터 없음")
    })
    public ResponseEntity<FieldExperience> moderate(
            @Parameter(description = "현장 체험 PK", example = "1", required = true) @PathVariable Long id,
            @Parameter(description = "변경할 모더레이션 상태", example = "APPROVED", required = true) @RequestParam FieldExperience.Moderation moderation,
            @Parameter(description = "반려 사유(반려 시 선택 사항)", example = "서류 누락") @RequestParam(required = false) String failReason) {
        FieldExperience updated = service.moderate(id, moderation, failReason);
        return ResponseEntity.ok(updated);
    }
}
