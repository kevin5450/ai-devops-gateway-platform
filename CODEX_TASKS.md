# CODEX_TASKS.md

이 파일은 Codex가 따라갈 작업 목록입니다.

Codex는 한 번에 하나의 Phase만 수행해야 합니다.

---

## Status Legend

| Mark | Meaning |
|---|---|
| `[ ]` | Not started |
| `[~]` | In progress |
| `[x]` | Done |
| `[!]` | Blocked or needs user confirmation |

---

# Phase 0. Repository Guidance

Goal:

```text
Codex가 이 프로젝트의 목표와 작업 규칙을 이해할 수 있도록 안내 파일을 추가한다.
```

Tasks:

- [ ] Add `AGENTS.md`
- [ ] Add `CODEX_TASKS.md`
- [ ] Add `CODE_REVIEW.md`
- [ ] Add `CODEX_PROMPTS.md`
- [ ] Add `CODEX_WORKFLOW.md`
- [ ] Commit and push guidance files

Expected commit:

```text
add codex project guidance
```

---

# Phase 1. Monorepo Structure

Goal:

```text
프로젝트 전체 폴더 구조를 만든다.
```

Create:

```text
apps/
  web-client/
  gateway-service/
  ai-service/
  log-service/

infra/
  docker/
  k8s/
  k6/

docs/

.github/
  workflows/
```

Tasks:

- [x] Create folders
- [x] Add `.gitkeep` files
- [x] Add `.gitignore`
- [x] Create `docs/architecture.md`
- [x] Create `docs/performance-report.md`
- [x] Create `docs/trouble-shooting.md`
- [x] Update README project structure

Expected commit:

```text
create monorepo project structure
```

---

# Phase 2. Spring Boot Gateway Service

Goal:

```text
메인 백엔드인 Spring Boot Gateway Service를 만든다.
```

Tasks:

- [x] Create Gradle Spring Boot project under `apps/gateway-service`
- [x] Use Java 21
- [x] Add `/health`
- [x] Add `/api/chat` placeholder endpoint
- [x] Add request DTO
- [x] Add response DTO
- [x] Add basic error response
- [x] Add local run guide to README
- [x] Add Gradle wrapper
- [x] Verify build with `.\gradlew.bat build`
- [x] Add Gateway API structure tests

Expected commit:

```text
add spring boot gateway service
```

---

# Phase 3. Forest IoT Sensor Base API

Goal:

```text
Forest IoT Monitoring 도메인의 센서 데이터 수집/조회 API를 Gateway Service에 추가한다.
```

Tasks:

- [x] Add `GET /api/health`
- [x] Add `POST /api/readings`
- [x] Add `GET /api/devices/{deviceId}/latest`
- [x] Add `GET /api/devices/{deviceId}/issues/latest`
- [x] Add sensor reading request/response DTOs
- [x] Add sensor reading domain objects
- [x] Add in-memory latest reading store
- [x] Add temperature/humidity threshold policy
- [x] Add validation and not-found error responses
- [x] Add Gateway API tests
- [x] Update README with current sensor API

Expected commit:

```text
add sensor reading base api
```

---

# Phase 4. Domain Separation and Tests

Goal:

```text
Forest IoT Monitoring 도메인 구조를 정리하고 검증/정책 테스트를 보강한다.
```

Tasks:

- [x] Add domain records for `DeviceId`, `SensorReading`, and `SensorIssue`
- [x] Add `IssueStatus` enum
- [x] Add `SensorThresholdPolicy`
- [x] Keep storage in-memory
- [x] Add normal reading ingestion test
- [x] Add invalid humidity test
- [x] Add missing deviceId test
- [x] Add negative light test
- [x] Add abnormal temperature issue test
- [x] Add abnormal humidity issue test
- [x] Add threshold policy unit tests
- [x] Verify build with `.\gradlew.bat build`

Expected commit:

```text
add sensor domain tests
```

---

# Phase 5. Persistence Preparation and Documentation

Goal:

```text
향후 MongoDB 연동과 운영 확장을 위한 구조와 문서를 준비한다.
```

Tasks:

- [x] Add `SensorReadingRepository` abstraction
- [x] Keep `InMemorySensorReadingStore` as the current implementation
- [x] Do not add MongoDB dependency yet
- [x] Document why DB integration is postponed
- [x] Add API examples to `docs/api-design.md`
- [x] Update architecture notes for the current in-memory boundary
- [x] Add troubleshooting notes for build, test, run, validation, and in-memory storage
- [x] Update README with package structure and persistence plan
- [x] Verify build with `.\gradlew.bat build`

Expected commit:

```text
prepare sensor persistence structure
```

---

# Phase 6. MongoDB Persistence Layer

Goal:

```text
Gateway Service에 MongoDB 저장소 구현체를 추가하되, 기본 in-memory 구조도 유지한다.
```

Tasks:

- [x] Add Spring Data MongoDB dependency to Gateway Service
- [x] Keep `SensorReadingRepository` as the service-facing boundary
- [x] Keep `InMemorySensorReadingStore` for the default profile
- [x] Add MongoDB document for sensor readings
- [x] Add MongoDB repository adapter
- [x] Use environment-backed MongoDB connection configuration
- [x] Do not create or commit `.env`
- [x] Add local configuration example without secrets
- [x] Add mapper test for persistence conversion
- [x] Update README and docs
- [x] Verify build with `.\gradlew.bat build`

Expected commit:

```text
add mongodb sensor reading persistence
```

---

# Phase 7. Kafka Event Flow

Goal:

```text
Gateway에서 요청 처리 결과를 Kafka 이벤트로 발행한다.
```

Tasks:

- [ ] Add Kafka producer to Gateway Service
- [ ] Define event schema
- [ ] Use topic `ai.request.events`
- [ ] Create or prepare Log Service Kafka consumer
- [ ] Log consumed events to console first
- [ ] Document topic and event schema

Expected commit:

```text
add kafka request event flow
```

---

# Phase 8. Log Service + MongoDB

Goal:

```text
Log Service가 Kafka 이벤트를 받아 MongoDB에 저장한다.
```

Tasks:

- [ ] Create Spring Boot Log Service under `apps/log-service`
- [ ] Add `/health`
- [ ] Add Kafka consumer
- [ ] Add MongoDB dependency
- [ ] Store request events in MongoDB
- [ ] Add `/logs/recent`
- [ ] Document MongoDB log schema

Expected commit:

```text
add log service mongodb storage
```

---

# Phase 9. Docker Compose Full Stack

Goal:

```text
로컬에서 전체 서비스를 Docker Compose로 실행한다.
```

Tasks:

- [ ] Add Dockerfile for `web-client`
- [ ] Add Dockerfile for `gateway-service`
- [ ] Add Dockerfile for `ai-service`
- [ ] Add Dockerfile for `log-service`
- [ ] Add `infra/docker/docker-compose.yml`
- [ ] Include Redis
- [ ] Include Kafka
- [ ] Include Zookeeper or Kafka Kraft mode
- [ ] Include MongoDB
- [ ] Add `.env.example`
- [ ] Document ports and run command

Expected commit:

```text
add docker compose full stack
```

---

# Phase 10. Kubernetes Manifests

Goal:

```text
Kubernetes 배포 구조를 manifest로 정리한다.
```

Tasks:

- [ ] Add namespace manifest
- [ ] Add Gateway deployment/service
- [ ] Add AI Service deployment/service
- [ ] Add Log Service deployment/service
- [ ] Add Web Client deployment/service
- [ ] Add Redis manifest
- [ ] Add MongoDB manifest or document limitation
- [ ] Add Kafka manifest or document limitation
- [ ] Document apply order

Expected commit:

```text
add kubernetes manifests
```

---

# Phase 11. GitHub Actions CI

Goal:

```text
GitHub Actions로 기본 빌드 검사를 자동화한다.
```

Tasks:

- [ ] Add Gateway Service build workflow
- [ ] Add Log Service build workflow
- [ ] Add Web Client build workflow
- [ ] Add AI Service check workflow
- [ ] Do not deploy automatically yet

Expected commit:

```text
add github actions ci
```

---

# Phase 12. k6 Load Test and Performance Report

Goal:

```text
/api/chat 응답 시간을 부하테스트하고 결과를 문서화한다.
```

Tasks:

- [ ] Add k6 script under `infra/k6`
- [ ] Test 10 virtual users
- [ ] Test 50 virtual users
- [ ] Test 100 virtual users
- [ ] Record avg, p95, p99
- [ ] Update `docs/performance-report.md`
- [ ] Explain bottlenecks and next improvements

Important:

```text
Do not invent measured results.
Only write actual results after running the test.
```

Expected commit:

```text
add k6 load test and performance report
```

---

# Phase 13. Final Documentation Polish

Goal:

```text
포트폴리오로 보여줄 수 있도록 README와 문서를 정리한다.
```

Tasks:

- [ ] Add architecture diagram
- [ ] Add local run guide
- [ ] Add API table
- [ ] Add troubleshooting summary
- [ ] Add performance summary
- [ ] Add portfolio summary paragraph
- [ ] Add limitations and future improvements

Expected commit:

```text
polish portfolio documentation
```
