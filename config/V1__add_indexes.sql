-- ============================================================
-- 인덱스 추가 마이그레이션 스크립트 (MySQL)
-- 생성일: 2026-08-21
-- ============================================================

-- 1. user_accounts 테이블
--    - email 유니크 인덱스: 로그인/회원가입 시 이메일 조회 최적화
--    - refreshToken 인덱스: 로그아웃 시 리프레시 토큰 조회 최적화
CREATE UNIQUE INDEX idx_user_accounts_email ON user_accounts (email);
CREATE INDEX idx_user_accounts_refresh_token ON user_accounts (refreshToken);

-- 2. field_experiences 테이블
--    - 복합 인덱스: 지점별 승인된 경험 조회 최적화
--    - 단일 인덱스: 심사 상태별 조회 최적화
--    (branchId, authorId, visitDate) 유니크 제약조건은 이미 존재
CREATE INDEX idx_field_exp_branch_moderation ON field_experiences (branchId, moderationStatus);
CREATE INDEX idx_field_exp_moderation_status ON field_experiences (moderationStatus);

-- 3. guide_contents 테이블
--    - contentType 인덱스: 콘텐츠 유형별 조회 최적화
CREATE INDEX idx_guide_content_type ON guide_contents (contentType);

-- 4. timeline_tasks 테이블
--    - 복합 인덱스: 사용자별 우선순위 정렬 조회 최적화
CREATE INDEX idx_timeline_user_priority ON timeline_tasks (userId, priority);
