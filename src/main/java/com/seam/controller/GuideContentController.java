package com.seam.controller;

import com.seam.entity.GuideContent;
import com.seam.service.GuideContentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/guides")
@Tag(name = "Guide Content", description = "안내 콘텐츠 동기화 API")
public class GuideContentController {
    private final GuideContentService service;

    public GuideContentController(GuideContentService service) {
        this.service = service;
    }

    @GetMapping("/sync")
    @Operation(summary = "가이드 콘텐츠 동기화", description = "콘텐츠 타입별 가이드 정보를 조회해 동기화합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = GuideContent.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    public ResponseEntity<List<GuideContent>> sync(
            @Parameter(description = "가이드 유형 필터(선택 사항)", example = "visa") @RequestParam(required = false) String type) {
        return ResponseEntity.ok(service.listByType(type));
    }
}
