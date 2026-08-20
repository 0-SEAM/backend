package com.seam.controller;

import com.seam.dto.TimelineTaskDto;
import com.seam.entity.TimelineTask;
import com.seam.service.TimelineService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/timelines")
@Tag(name = "Timeline", description = "사용자별 타임라인 및 작업 상태 관리 API")
public class TimelineController {
        private final TimelineService service;

        public TimelineController(TimelineService service) {
                this.service = service;
        }

        @GetMapping("/{userId}")
        @Operation(summary = "사용자 타임라인 조회", description = "해당 사용자의 타임라인 작업 목록을 조회합니다. 본인만 접근할 수 있습니다.")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = TimelineTask.class))),
                        @ApiResponse(responseCode = "403", description = "본인이 아닌 사용자 접근"),
                        @ApiResponse(responseCode = "404", description = "사용자 정보 없음")
        })
        public ResponseEntity<List<TimelineTask>> get(
                        @Parameter(description = "조회 대상 사용자 ID", example = "user-001", required = true) @PathVariable String userId,
                        Authentication authentication) {
                String requester = authentication.getName();
                if (!userId.equals(requester))
                        return ResponseEntity.status(403).build();
                return ResponseEntity.ok(service.getTimeline(userId));
        }

        @PostMapping("/{userId}/tasks")
        @Operation(summary = "타임라인 작업 생성", description = "사용자의 타임라인에 새 작업을 생성합니다.")
        @ApiResponses({
                        @ApiResponse(responseCode = "201", description = "작업 생성 성공", content = @Content(schema = @Schema(implementation = TimelineTask.class))),
                        @ApiResponse(responseCode = "400", description = "요청 값 검증 실패"),
                        @ApiResponse(responseCode = "403", description = "본인이 아닌 사용자 접근")
        })
        public ResponseEntity<TimelineTask> create(
                        @Parameter(description = "작업을 생성할 사용자 ID", example = "user-001", required = true) @PathVariable String userId,
                        @Valid @RequestBody TimelineTaskDto dto,
                        Authentication authentication) {
                String requester = authentication.getName();
                if (!userId.equals(requester))
                        return ResponseEntity.status(403).build();
                TimelineTask t = new TimelineTask();
                t.setUserId(userId);
                t.setTaskType(dto.getTaskType());
                t.setPriority(dto.getPriority());
                t.setPrerequisiteTaskIds(dto.getPrerequisiteTaskIds());
                TimelineTask saved = service.createTask(t);
                return ResponseEntity.created(URI.create("/api/timelines/" + userId + "/tasks/" + saved.getTaskId()))
                                .body(saved);
        }

        @PostMapping("/{userId}/tasks/{taskId}/status")
        @Operation(summary = "타임라인 작업 상태 갱신", description = "특정 작업의 상태를 진행 중, 완료 등으로 업데이트합니다.")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "상태 갱신 성공", content = @Content(schema = @Schema(implementation = TimelineTask.class))),
                        @ApiResponse(responseCode = "403", description = "본인이 아닌 사용자 접근"),
                        @ApiResponse(responseCode = "404", description = "작업 정보 없음")
        })
        public ResponseEntity<TimelineTask> updateStatus(
                        @Parameter(description = "작업 소유 사용자 ID", example = "user-001", required = true) @PathVariable String userId,
                        @Parameter(description = "작업 PK", example = "10", required = true) @PathVariable Long taskId,
                        @Parameter(description = "변경할 작업 상태", example = "DONE", required = true) @RequestParam TimelineTask.Status status,
                        Authentication authentication) {
                String requester = authentication.getName();
                if (!userId.equals(requester))
                        return ResponseEntity.status(403).build();
                TimelineTask updated = service.updateTaskStatus(taskId, status);
                return ResponseEntity.ok(updated);
        }
}
