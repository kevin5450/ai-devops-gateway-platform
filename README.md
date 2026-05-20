# ai-devops-gateway-platform

React, Spring Boot, AI Service, Redis, Kafka, MongoDB를 조합해 DevOps 관점의 Gateway 플랫폼을 구성하는 monorepo 프로젝트입니다.

현재 도메인은 Forest IoT Monitoring입니다. Gateway Service가 현장 장비의 센서 데이터를 수집하고, 장비별 최신값과 온도/습도 이상 상태를 조회하는 API를 제공합니다.

## 최종 목표

- React 프론트엔드
- Spring Boot Gateway Service
- Node.js 또는 Python 기반 AI Service
- Spring Boot Log Service
- Redis 기반 캐시 및 Rate Limit
- Kafka 기반 비동기 로그 이벤트 처리
- MongoDB 기반 로그 저장
- Docker Compose 기반 로컬 실행
- Kubernetes manifest 기반 배포 구조
- GitHub Actions CI/CD
- k6 기반 부하테스트

## 예정 아키텍처

```text
Web Client (React)
        |
        v
Gateway Service (Spring Boot)
        |
        +--> AI Service (Node.js or Python)
        |
        +--> Redis (Cache / Rate Limit)
        |
        +--> Kafka (Async Log Events)
                 |
                 v
          Log Service (Spring Boot)
                 |
                 v
              MongoDB
```

## 프로젝트 구조

```text
ai-devops-gateway-platform/
├── apps/
│   ├── web-client/
│   ├── gateway-service/
│   ├── ai-service/
│   └── log-service/
├── infra/
│   ├── docker/
│   ├── k8s/
│   └── k6/
├── docs/
│   ├── architecture.md
│   ├── performance-report.md
│   └── trouble-shooting.md
├── .github/
│   └── workflows/
├── .gitignore
└── README.md
```

## 디렉터리 역할

- `apps/web-client`: React 기반 웹 클라이언트
- `apps/gateway-service`: Spring Boot 기반 API Gateway 서비스
- `apps/ai-service`: Node.js 또는 Python 기반 AI 처리 서비스
- `apps/log-service`: Spring Boot 기반 로그 처리 서비스
- `infra/docker`: Docker Compose 및 로컬 실행 관련 파일
- `infra/k8s`: Kubernetes manifest 파일
- `infra/k6`: k6 부하테스트 스크립트
- `docs`: 아키텍처, 성능 리포트, 트러블슈팅 문서
- `.github/workflows`: GitHub Actions CI/CD 워크플로

## 현재 상태

- Monorepo 기본 폴더 구조 정의
- Git 추적을 위한 `.gitkeep` 파일 배치
- Gateway Service 기본 Spring Boot 프로젝트 생성
- Gateway Service에 Forest IoT 센서 기본 API 추가
- React, AI Service, Kafka, Redis, MongoDB 코드는 아직 없음

## Gateway Service

`apps/gateway-service`는 Java 21, Spring Boot, Gradle 기반의 API Gateway 서비스입니다.

현재 제공하는 엔드포인트:

- `GET /health`: Gateway Service 상태 확인
- `GET /api/health`: Gateway Service 상태 확인
- `POST /api/chat`: AI Service 연동 전 placeholder 응답 반환
- `POST /api/readings`: 센서 측정값 수집
- `GET /api/devices/{deviceId}/latest`: 장비별 최신 센서 측정값 조회
- `GET /api/devices/{deviceId}/issues/latest`: 장비별 최신 온도/습도 이상 상태 조회

현재 Gateway Service는 Controller, Service, DTO, Domain, Repository, Exception Handler 패키지로 기본 API 구조를 분리합니다.

기본 실행에서는 센서 데이터를 in-memory 저장소에 장비별 최신값으로 보관합니다. `mongo` 프로필을 활성화하면 같은 `SensorReadingRepository` 인터페이스를 통해 MongoDB 저장소 구현체를 사용할 수 있습니다.

AI placeholder 예시 요청:

```bash
curl -X POST http://localhost:8080/api/chat \
  -H "Content-Type: application/json" \
  -d "{\"prompt\":\"hello\",\"userId\":\"demo-user\"}"
```

센서 측정값 생성 예시:

```bash
curl -X POST http://localhost:8080/api/readings \
  -H "Content-Type: application/json" \
  -d "{\"deviceId\":\"Cube1\",\"measuredAt\":\"2026-05-19T15:30:00\",\"temperature\":24.5,\"humidity\":61.2,\"light\":832.5}"
```

최신 측정값 조회:

```bash
curl http://localhost:8080/api/devices/Cube1/latest
```

최신 이상 상태 조회:

```bash
curl http://localhost:8080/api/devices/Cube1/issues/latest
```

센서 검증 규칙:

| Field | Rule |
|---|---|
| `deviceId` | required |
| `measuredAt` | required, ISO-8601 local date-time |
| `temperature` | required |
| `humidity` | required, 0~100 |
| `light` | required, 0 이상 |

이상 상태 기준:

| Metric | Normal Range |
|---|---|
| temperature | 18~27 |
| humidity | 40~75 |

현재 패키지 구조:

```text
com.aidevops.gateway
├── controller
├── domain
├── dto
├── exception
├── repository
└── service
```

MongoDB 저장소 구조:

- 기본 프로필: `InMemorySensorReadingStore`
- `mongo` 프로필: `MongoSensorReadingRepository`
- MongoDB collection: `sensor_readings`
- 연결 정보: `MONGODB_URI` 환경변수로 주입

MongoDB 실행 예시:

```powershell
cd apps/gateway-service
$env:SPRING_PROFILES_ACTIVE="mongo"
$env:MONGODB_URI="mongodb://localhost:27017/forest_iot_gateway"
.\gradlew.bat bootRun
```

민감정보는 코드, 문서, `.env` 파일에 저장하지 않습니다.

로컬 실행:

```powershell
cd apps/gateway-service
.\gradlew.bat bootRun
```

빌드:

```powershell
cd apps/gateway-service
.\gradlew.bat build
```

Gateway Service는 Gradle wrapper를 포함하므로 별도 Gradle 설치 없이 Windows에서 `.\gradlew.bat` 명령으로 실행할 수 있습니다.
