# 0:SEAM Backend

Spring Boot 3.2 + Java 21 기반 백엔드 서버.

---

## 빌드

```bash
mvn clean package          # 테스트 포함 빌드
mvn clean package -DskipTests  # 테스트 제외 빌드
```

## 실행

```bash
mvn spring-boot:run        # 로컬 (H2 인메모리 DB)
```

### Docker

```bash
docker compose up --build -d
docker compose logs -f backend
docker compose down
```

## 테스트

```bash
mvn test                   # 전체 테스트 실행
mvn test -pl . -Dtest=PiiUtilsTest  # 특정 클래스만 실행
```

## 코드 검사

| 도구       | 명령어                 | 설정 파일                            |
| ---------- | ---------------------- | ------------------------------------ |
| Checkstyle | `mvn checkstyle:check` | `config/checkstyle/checkstyle.xml`   |
| SpotBugs   | `mvn spotbugs:check`   | `config/spotbugs/exclude-filter.xml` |
| PMD        | `mvn pmd:check`        | `config/pmd/ruleset.xml`             |

세 검사를 한 번에 실행:

```bash
mvn checkstyle:check spotbugs:check pmd:check
```

---

## API 엔드포인트

모든 개인 데이터 API는 다음 헤더가 필요합니다.

```
Authorization: Bearer <accessToken>
```

### Authentication (`/api/auth`)

| 메서드 | 경로                | 설명                                                  | 인증 |
| ------ | ------------------- | ----------------------------------------------------- | ---- |
| POST   | `/api/auth/signup`  | 회원가입. 이메일·비밀번호 검증 후 JWT 발급            | ✗    |
| POST   | `/api/auth/login`   | 로그인. 비밀번호 확인 후 JWT 발급                     | ✗    |
| POST   | `/api/auth/refresh` | Access token 갱신. Refresh token 검증 후 새 토큰 발급 | ✗    |
| POST   | `/api/auth/logout`  | Refresh token 폐기                                    | ✗    |

### User Condition (`/api/conditions`)

| 메서드 | 경로                       | 설명                                      | 인증 |
| ------ | -------------------------- | ----------------------------------------- | ---- |
| POST   | `/api/conditions`          | 사용자 비자·거주·근무 조건 생성 또는 갱신 | ✓    |
| GET    | `/api/conditions/{userId}` | 특정 사용자의 조건 정보 조회 (본인만)     | ✓    |
| PUT    | `/api/conditions/{userId}` | 기존 사용자 조건 전체 수정 (본인만)       | ✓    |

### Guide Content (`/api/guides`)

| 메서드 | 경로                           | 설명                                              | 인증 |
| ------ | ------------------------------ | ------------------------------------------------- | ---- |
| GET    | `/api/guides/sync?type={type}` | 콘텐츠 타입별 가이드 조회. `type`은 선택 파라미터 | ✗    |

### Timeline (`/api/timelines`)

| 메서드 | 경로                                                            | 설명                                           | 인증 |
| ------ | --------------------------------------------------------------- | ---------------------------------------------- | ---- |
| GET    | `/api/timelines/{userId}`                                       | 사용자 타임라인 작업 목록 조회 (본인만)        | ✓    |
| POST   | `/api/timelines/{userId}/tasks`                                 | 타임라인에 새 작업 생성 (본인만)               | ✓    |
| POST   | `/api/timelines/{userId}/tasks/{taskId}/status?status={STATUS}` | 작업 상태 갱신 (`TODO`, `IN_PROGRESS`, `DONE`) | ✓    |

### Field Experience (`/api/experiences`)

| 메서드 | 경로                                                 | 설명                                   | 인증      |
| ------ | ---------------------------------------------------- | -------------------------------------- | --------- |
| POST   | `/api/experiences`                                   | 현장 체험 신청 등록                    | ✓         |
| GET    | `/api/experiences?branchId={branchId}`               | 브랜치별 승인된 체험 목록 조회         | ✗         |
| GET    | `/api/experiences/pending`                           | 검토 대기 중인 체험 목록 조회          | ✓ (ADMIN) |
| POST   | `/api/experiences/{id}/moderate?moderation={STATUS}` | 승인(`APPROVED`)/반려(`REJECTED`) 처리 | ✓ (ADMIN) |

---

## 환경변수

| 변수명                      | 설명                              | 기본값                                                |
| --------------------------- | --------------------------------- | ----------------------------------------------------- |
| `DB_URL`                    | JDBC 연결 문자열                  | `jdbc:h2:mem:seamdb;DB_CLOSE_DELAY=-1`                |
| `DB_DRIVER`                 | JDBC 드라이버 클래스              | `org.h2.Driver`                                       |
| `DB_USERNAME`               | 데이터베이스 사용자               | `sa`                                                  |
| `DB_PASSWORD`               | 데이터베이스 비밀번호             | _(빈 값)_                                             |
| `H2_CONSOLE_ENABLED`        | H2 콘솔 활성화 여부               | `true`                                                |
| `JWT_SECRET`                | JWT 서명용 시크릿 (최소 32바이트) | `local-development-secret-change-me-32-bytes-minimum` |
| `JWT_ACCESS_EXPIRATION_MS`  | Access token 유효기간 (ms)        | `900000` (15분)                                       |
| `JWT_REFRESH_EXPIRATION_MS` | Refresh token 유효기간 (ms)       | `1209600000` (14일)                                   |
| `CORS_ALLOWED_ORIGINS`      | 허용 오리진 (쉼표 구분)           | `http://localhost:5173`                               |

운영 환경에서는 `JWT_SECRET`과 `DB_PASSWORD`를 반드시 별도로 설정해야 합니다.
