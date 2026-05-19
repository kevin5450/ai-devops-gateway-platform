# CODEX_PROMPTS.md

이 파일은 Codex에게 그대로 복사해서 넣을 수 있는 단계별 프롬프트 모음입니다.

중요:

```text
Codex에게 한 번에 전체 프로젝트를 만들라고 하지 않는다.
반드시 Phase 하나씩 진행한다.
```

---

# Prompt 0. Check Project Guidance

```text
Read AGENTS.md, CODEX_TASKS.md, CODE_REVIEW.md, and CODEX_WORKFLOW.md first.

Do not modify files yet.

Confirm:
1. current project goal
2. tech stack
3. current task status
4. next safest task

Then suggest the next prompt from CODEX_PROMPTS.md.
```

---

# Prompt 1. Create Monorepo Structure

```text
Read AGENTS.md, CODEX_TASKS.md, CODE_REVIEW.md, and CODEX_WORKFLOW.md.

Implement Phase 1 only.

Create the monorepo folder structure:
- apps/web-client
- apps/gateway-service
- apps/ai-service
- apps/log-service
- infra/docker
- infra/k8s
- infra/k6
- docs
- .github/workflows

Add .gitkeep files where needed.

Create or update .gitignore for:
- Java
- Gradle
- Node.js
- IntelliJ
- VS Code
- Docker
- env files

Create:
- docs/architecture.md
- docs/performance-report.md
- docs/trouble-shooting.md

Update README.md with the project structure.

Do not create Spring Boot or React app code yet.

Update CODEX_TASKS.md after completion.

Run:
- git status

Summarize changed files and suggest a commit message.
Do not push to GitHub.
```

---

# Prompt 2. Create Spring Boot Gateway Service

```text
Read AGENTS.md, CODEX_TASKS.md, CODE_REVIEW.md, and CODEX_WORKFLOW.md.

Implement Phase 2 only.

Create a Gradle-based Java 21 Spring Boot project under apps/gateway-service.

Required:
- /health endpoint
- /api/chat placeholder endpoint
- request DTO
- response DTO
- simple error response structure
- local run guide in README

Do not add Redis, Kafka, Docker, or AI Service integration yet.

Run the relevant build/check command if possible.

Update CODEX_TASKS.md.

Show:
1. changed files
2. checks run
3. possible risks
4. suggested commit message

Do not push to GitHub.
```

---

# Prompt 3. Create React Web Client

```text
Read AGENTS.md, CODEX_TASKS.md, CODE_REVIEW.md, and CODEX_WORKFLOW.md.

Implement Phase 3 only.

Create a React + Vite app under apps/web-client.

Required:
- prompt input
- submit button
- call gateway-service /api/chat
- display response
- display loading state
- display error state
- display latency or request status

Do not add authentication or advanced styling.

Run the relevant build/check command if possible.

Update CODEX_TASKS.md.

Show:
1. changed files
2. checks run
3. possible risks
4. suggested commit message

Do not push to GitHub.
```

---

# Prompt 4. Create AI Service

```text
Read AGENTS.md, CODEX_TASKS.md, CODE_REVIEW.md, and CODEX_WORKFLOW.md.

Implement Phase 4 only.

Create AI Service under apps/ai-service.

Use Node.js Express unless there is a strong reason to choose another stack.

Required:
- GET /health
- POST /ai/chat
- mock response first
- environment variable structure for future LLM provider/API key
- no hardcoded secrets

Run the relevant local check if possible.

Update CODEX_TASKS.md.

Show:
1. changed files
2. checks run
3. possible risks
4. suggested commit message

Do not push to GitHub.
```

---

# Prompt 5. Connect Gateway to AI Service

```text
Read AGENTS.md, CODEX_TASKS.md, CODE_REVIEW.md, and CODEX_WORKFLOW.md.

Implement Phase 5 only.

Modify gateway-service so /api/chat forwards requests to ai-service.

Required:
- HTTP client call from gateway-service to ai-service
- configurable AI_SERVICE_BASE_URL
- timeout handling
- latencyMs measurement
- fallback error response

Update README and CODEX_TASKS.md.

Run the relevant build/check command if possible.

Show:
1. changed files
2. checks run
3. possible risks
4. suggested commit message

Do not push to GitHub.
```

---

# Prompt 6. Add Redis Rate Limiting

```text
Read AGENTS.md, CODEX_TASKS.md, CODE_REVIEW.md, and CODEX_WORKFLOW.md.

Implement Phase 6 only.

Add Redis-based simple rate limiting to gateway-service.

Required:
- userId or IP based limit
- configurable Redis host/port
- clear error response when limit exceeded
- document the rule in README

Do not add Kafka yet.

Update CODEX_TASKS.md.

Run build/check if possible.

Show:
1. changed files
2. checks run
3. possible risks
4. suggested commit message

Do not push to GitHub.
```

---

# Prompt 7. Add Kafka Event Flow

```text
Read AGENTS.md, CODEX_TASKS.md, CODE_REVIEW.md, and CODEX_WORKFLOW.md.

Implement Phase 7 only.

Add Kafka request-event publishing from gateway-service.

Create or prepare log-service as Kafka consumer if not already created.

Required:
- topic name: ai.request.events
- event schema documented
- producer sends request result event
- consumer logs event to console first

Do not add MongoDB persistence yet.

Update CODEX_TASKS.md.

Run build/check if possible.

Show:
1. changed files
2. checks run
3. possible risks
4. suggested commit message

Do not push to GitHub.
```

---

# Prompt 8. Add Log Service MongoDB Storage

```text
Read AGENTS.md, CODEX_TASKS.md, CODE_REVIEW.md, and CODEX_WORKFLOW.md.

Implement Phase 8 only.

Create or complete Spring Boot log-service.

Required:
- /health endpoint
- Kafka consumer
- MongoDB storage for request logs
- /logs/recent endpoint
- log schema documentation

Update CODEX_TASKS.md.

Run build/check if possible.

Show:
1. changed files
2. checks run
3. possible risks
4. suggested commit message

Do not push to GitHub.
```

---

# Prompt 9. Add Docker Compose Full Stack

```text
Read AGENTS.md, CODEX_TASKS.md, CODE_REVIEW.md, and CODEX_WORKFLOW.md.

Implement Phase 9 only.

Add Dockerfiles and docker-compose.yml.

Required services:
- web-client
- gateway-service
- ai-service
- log-service
- redis
- kafka
- zookeeper or kraft mode
- mongodb

Add .env.example.

Document local run command and ports.

Update CODEX_TASKS.md.

If possible, run:
- docker compose -f infra/docker/docker-compose.yml config

Show:
1. changed files
2. checks run
3. possible risks
4. suggested commit message

Do not push to GitHub.
```

---

# Prompt 10. Add Kubernetes Manifests

```text
Read AGENTS.md, CODEX_TASKS.md, CODE_REVIEW.md, and CODEX_WORKFLOW.md.

Implement Phase 10 only.

Add simple Kubernetes manifests under infra/k8s.

Required:
- namespace
- deployment/service for web-client
- deployment/service for gateway-service
- deployment/service for ai-service
- deployment/service for log-service
- Redis manifest
- MongoDB manifest or documented local limitation
- Kafka manifest or documented local limitation

Do not use Helm.

Update README and CODEX_TASKS.md.

Show:
1. changed files
2. checks run
3. possible risks
4. suggested commit message

Do not push to GitHub.
```

---

# Prompt 11. Add GitHub Actions CI

```text
Read AGENTS.md, CODEX_TASKS.md, CODE_REVIEW.md, and CODEX_WORKFLOW.md.

Implement Phase 11 only.

Add GitHub Actions workflows.

Required:
- build/check gateway-service
- build/check log-service
- build/check web-client
- build/check ai-service
- no production deployment yet

Update CODEX_TASKS.md.

Show:
1. changed files
2. checks run
3. possible risks
4. suggested commit message

Do not push to GitHub.
```

---

# Prompt 12. Add k6 Load Test and Report

```text
Read AGENTS.md, CODEX_TASKS.md, CODE_REVIEW.md, and CODEX_WORKFLOW.md.

Implement Phase 12 only.

Add k6 scripts under infra/k6.

Required:
- test /api/chat
- scenarios for 10, 50, 100 virtual users
- collect avg, p95, p99
- update docs/performance-report.md with instructions and result table template

Do not invent measured results.
Only add placeholders unless an actual test was run.

Update CODEX_TASKS.md.

Show:
1. changed files
2. checks run
3. possible risks
4. suggested commit message

Do not push to GitHub.
```

---

# Prompt 13. Final Documentation Polish

```text
Read AGENTS.md, CODEX_TASKS.md, CODE_REVIEW.md, and CODEX_WORKFLOW.md.

Implement Phase 13 only.

Polish documentation for portfolio presentation.

Required:
- README architecture summary
- local run guide
- API table
- Docker Compose guide
- Kubernetes guide
- performance test guide
- troubleshooting summary
- limitations
- future improvements
- portfolio summary paragraph

Do not invent performance results.

Update CODEX_TASKS.md.

Show:
1. changed files
2. checks run
3. possible risks
4. suggested commit message

Do not push to GitHub.
```
