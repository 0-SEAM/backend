# SEAM Backend (Spring Boot)

간단한 로컬 실행용 Spring Boot 프로젝트 템플릿입니다.

## 프로젝트 개요

이 프로젝트는 사용자 조건, 안내 콘텐츠, 타임라인, 현장 체험 등의 정보를 관리하는 Spring Boot 백엔드입니다.
프론트엔드에서 화면을 구성할 때 필요한 API와 데이터 흐름을 백엔드에서 정리해 두었으며, 각 기능은 Controller → Service → Entity/Repository 구조로 연결됩니다.

## 문서와 코드 연결 설명

작성해둔 Markdown 문서(요구사항/기획 문서)는 프론트에서 "어떤 화면에서 어떤 데이터를 보여줘야 하는지"를 설명하는 기준이 되고, 실제 구현은 각 API 엔드포인트로 연결됩니다. 즉, 문서의 항목 하나하나가 백엔드 Controller의 API 하나와 연결되며, 프론트는 이 API를 호출해서 화면을 구성합니다.

### 핵심 연결 예시

- 사용자 조건 관리
  - 기능: 비자, 거주지, 근무지, ARC 만료일 등 사용자 상태 관리
  - 백엔드: `UserConditionController` (`/api/conditions`)
  - 연결 포인트: 프론트의 사용자 프로필/설정 화면

- 안내 콘텐츠
  - 기능: 가이드/안내 문구, 카테고리별 콘텐츠 동기화
  - 백엔드: `GuideContentController` (`/api/guides/sync`)
  - 연결 포인트: 프론트의 가이드/안내 화면

- 타임라인
  - 기능: 사용자별 작업 목록, 상태 변경, 우선순위 및 선행 작업 관리
  - 백엔드: `TimelineController` (`/api/timelines/{userId}`)
  - 연결 포인트: 프론트의 마이페이지/진행 상황 화면

- 현장 체험
  - 기능: 신청, 목록 조회, 승인/반려 처리
  - 백엔드: `FieldExperienceController` (`/api/experiences`)
  - 연결 포인트: 프론트의 체험 신청 및 관리자 승인 화면

### 실제 흐름

문서에서 "사용자 프로필 조건을 입력/수정한다"는 요구사항이 있으면,
`UserConditionController`의 POST/GET/PUT API로 연결되고,
그다음 `UserConditionService`가 비즈니스 로직을 처리하고,
`UserCondition` 엔티티와 `UserConditionRepository`가 데이터 저장소와 연결됩니다.

이 구조를 이해하면 프론트엔드 개발자는 어떤 화면이 어떤 API를 호출하는지 빠르게 파악할 수 있고, 기획 문서와 구현 코드를 같은 기준으로 추적할 수 있습니다.

## 실행 방법

Run:

```bash
mvn spring-boot:run
```

### Docker Compose로 실행

Docker Compose는 Spring Boot와 MySQL을 함께 실행하며, MySQL 데이터는 `mysql-data` 볼륨에 보존됩니다.

```bash
docker compose up --build -d
```

백엔드는 `http://localhost:8080`, MySQL은 `localhost:3306`에서 접근할 수 있습니다.
컨테이너 로그 확인과 종료는 다음 명령으로 실행합니다.

```bash
docker compose logs -f backend
docker compose down
```

기본 데이터베이스 값은 개발용으로 `seamdb` / `seam` / `seam-password`입니다.
실행 전에 `.env` 또는 셸 환경변수로 `MYSQL_DATABASE`, `MYSQL_USER`, `MYSQL_PASSWORD`,
`MYSQL_ROOT_PASSWORD`를 지정하면 Compose와 Spring Boot에 같은 값이 전달됩니다.

## 인증 API

회원가입 또는 로그인을 하면 백엔드가 access token과 refresh token을 반환합니다.
개인 데이터 API는 이후 요청부터 다음 헤더를 사용합니다.

```http
Authorization: Bearer <accessToken>
```

```http
POST /api/auth/signup
POST /api/auth/login
POST /api/auth/refresh
POST /api/auth/logout
```

프론트엔드는 `VITE_API_BASE_URL`을 백엔드 주소로 설정해야 합니다. access token이 만료되면 refresh token으로 한 번 갱신한 뒤 원 요청을 재시도합니다.

## 운영 환경변수

가비아 서버에서는 값을 소스에 저장하지 않고 프로세스 환경변수로 주입합니다.

```properties
DB_URL=jdbc:mysql://<host>:3306/<database>
DB_USERNAME=<database-user>
DB_PASSWORD=<database-password>
JWT_SECRET=<32바이트 이상 무작위 값>
JWT_ACCESS_EXPIRATION_MS=900000
JWT_REFRESH_EXPIRATION_MS=1209600000
CORS_ALLOWED_ORIGINS=https://<frontend-domain>
```

`JWT_SECRET`, `DB_PASSWORD`는 Git에 커밋하지 않습니다. 현재 기본 H2 설정은 로컬 개발용이며 운영에서는 가비아 DB 연결 정보로 바꿔야 합니다.

## 참고

- API 문서: Swagger/OpenAPI 기반으로 각 Controller 설명 제공
- 주요 패키지: `controller`, `service`, `dto`, `entity`, `repository`
