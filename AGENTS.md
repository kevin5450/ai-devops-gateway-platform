# AGENTS.md

이 파일은 Codex가 이 저장소에서 작업할 때 먼저 읽는 기본 작업 규칙입니다.

Codex는 이 파일을 기준으로 프로젝트 목표, 기술 스택, 작업 방식, 금지 사항을 이해해야 합니다.

---

## 1. Project Identity

Repository name:

```text
ai-devops-gateway-platform
```

Project goal:

```text
Spring Boot 기반 AI Gateway를 중심으로 React, Redis, Kafka, MongoDB, Docker, Kubernetes, GitHub Actions, k6를 연결한 AI DevOps 포트폴리오 프로젝트를 1개월 안에 구현한다.
```

이 프로젝트는 실무 수준의 대규모 엔터프라이즈 시스템을 완전히 재현하는 것이 아니라, 다음 역량을 보여주는 포트폴리오형 MVP입니다.

- Spring Boot 기반 백엔드 API 설계
- 간단한 MSA 구조 이해
- AI Service 분리
- Redis 기반 캐시 또는 Rate Limit
- Kafka 기반 비동기 이벤트 처리
- MongoDB 기반 로그 저장
- Docker Compose 기반 로컬 실행
- Kubernetes manifest 기반 배포 구조
- GitHub Actions 기반 CI
- k6 기반 부하테스트 및 P95/P99 분석
- README, Architecture, Troubleshooting, Performance Report 문서화

---

## 2. Core Tech Stack

| Area | Stack |
|---|---|
| Frontend | React + Vite |
| Gateway Service | Java 21 + Spring Boot + Gradle |
| Log Service | Java 21 + Spring Boot + Gradle |
| AI Service | Node.js Express 또는 Python FastAPI |
| Cache / Rate Limit | Redis |
| Event Streaming | Kafka |
| Database | MongoDB |
| Local Runtime | Docker Compose |
| Deployment Structure | Kubernetes manifests |
| CI/CD | GitHub Actions |
| Load Test | k6 |

---

## 3. Working Rules

Codex must follow these rules.

1. Work on only one phase at a time.
2. Before editing files, inspect the current repository structure.
3. Do not rewrite the whole project unless explicitly requested.
4. Preserve existing code and documentation unless a task requires changing them.
5. Prefer small, reviewable changes.
6. After each meaningful task, update `CODEX_TASKS.md`.
7. After implementing code, run the relevant local checks when possible.
8. Do not push to GitHub unless the user explicitly asks.
9. Before suggesting a commit, run or show:
   - `git status`
   - changed files summary
   - checks performed
10. Suggest a clear commit message after each task.

---

## 4. Commit Message Style

Use simple imperative commit messages.

Examples:

```text
add codex project guidance
create monorepo project structure
add spring boot gateway service
add react web client
add ai service mock endpoint
connect gateway to ai service
add redis rate limiting
add kafka request event flow
add log service mongodb storage
add docker compose full stack
add kubernetes manifests
add github actions ci
add k6 load test
polish portfolio documentation
```

---

## 5. Java / Spring Boot Rules

- Use Gradle, not Maven.
- Use Java 21.
- Keep package names simple and consistent.
- Each Spring Boot service must expose `/health`.
- Gateway Service must expose `/api/chat`.
- Log Service must expose `/logs/recent` after MongoDB integration.
- Separate controllers, services, DTOs, config, and exception handling.
- Use environment variables for service URLs and infrastructure connection values.
- Do not hardcode secrets.
- Use Spring Boot Actuator only when helpful.
- Avoid overengineering.

---

## 6. React Rules

- Use React + Vite.
- Keep UI simple and functional.
- The frontend should prove the backend flow.

Minimum UI:

- Prompt input
- Submit button
- Response display area
- Loading state
- Error state
- Latency or status display

Advanced UI is not required in the first version.

---

## 7. AI Service Rules

The AI Service may use Node.js Express or Python FastAPI.

Initial implementation must use a mock response first.

Required endpoints:

```text
GET /health
POST /ai/chat
```

The service must prepare environment variable structure for future LLM API use.

Do not hardcode API keys.

---

## 8. Redis Rules

Initial Redis usage should be simple.

Primary purpose:

```text
Rate limit per userId or IP
```

Optional later purpose:

```text
Cache repeated AI responses
```

The rate limit rule must be documented in README.

---

## 9. Kafka Rules

Kafka is used for asynchronous request event flow.

Initial flow:

```text
gateway-service -> Kafka topic -> log-service
```

Recommended topic name:

```text
ai.request.events
```

Each event should include:

```text
requestId
userId
serviceName
provider
promptLength
status
latencyMs
createdAt
```

First implementation can print consumed events to console.
MongoDB persistence can be added in the next phase.

---

## 10. MongoDB Rules

MongoDB is used for usage logs and latency records.

Recommended collection:

```text
request_logs
```

Recommended document fields:

```text
requestId
userId
serviceName
provider
promptLength
status
latencyMs
createdAt
errorMessage
```

---

## 11. Docker / Kubernetes Rules

Docker Compose must support local full-stack execution.

Kubernetes manifests should be simple and readable.

Do not introduce Helm unless explicitly requested.

Recommended folders:

```text
infra/docker
infra/k8s
```

---

## 12. Documentation Rules

Keep documentation updated as the project evolves.

Required docs:

```text
README.md
docs/architecture.md
docs/performance-report.md
docs/trouble-shooting.md
CODEX_TASKS.md
```

Do not invent performance results.
Only write actual measured results after running k6.
