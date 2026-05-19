# CODEX_WORKFLOW.md

이 파일은 사용자가 Codex와 함께 프로젝트를 진행하는 절차입니다.

핵심 원칙:

```text
Codex에게 구현과 점검을 맡긴다.
커밋과 push는 사용자가 확인 후 진행한다.
```

---

## 1. Why This Workflow Exists

이 프로젝트는 범위가 큽니다.

포함되는 요소:

- Spring Boot
- React
- AI Service
- Redis
- Kafka
- MongoDB
- Docker
- Kubernetes
- GitHub Actions
- k6
- 문서화

따라서 매번 ChatGPT에게 설명하고 다시 Codex에게 전달하면 흐름이 끊깁니다.

그래서 저장소 안에 다음 파일을 둡니다.

```text
AGENTS.md
CODEX_TASKS.md
CODE_REVIEW.md
CODEX_PROMPTS.md
CODEX_WORKFLOW.md
```

이 파일들이 Codex의 작업 기준이 됩니다.

---

## 2. File Roles

| File | Role |
|---|---|
| `AGENTS.md` | Codex가 지켜야 할 기본 규칙 |
| `CODEX_TASKS.md` | 전체 개발 로드맵 |
| `CODE_REVIEW.md` | 작업 후 점검 기준 |
| `CODEX_PROMPTS.md` | Codex에게 복사해서 넣을 단계별 명령문 |
| `CODEX_WORKFLOW.md` | 사용자와 Codex의 협업 절차 |

---

## 3. Daily Workflow

매일 또는 작업 시작 시 다음 순서로 진행합니다.

### Step 1. VS Code 열기

```powershell
cd D:\projects\ai-devops-gateway-platform
code .
```

### Step 2. 현재 Git 상태 확인

```powershell
git status
git pull
```

### Step 3. Codex 열기

VS Code에서 Codex 패널을 엽니다.

### Step 4. Codex에게 현재 상태 확인시키기

Codex에 입력:

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

### Step 5. CODEX_PROMPTS.md에서 다음 Phase 프롬프트 실행

예:

```text
Prompt 1. Create Monorepo Structure
```

한 번에 하나만 실행합니다.

### Step 6. Codex가 수정한 파일 확인

```powershell
git status
git diff --stat
```

필요하면 세부 diff 확인:

```powershell
git diff
```

### Step 7. 빌드 또는 테스트 확인

예:

```powershell
cd apps/gateway-service
gradlew.bat build
```

또는:

```powershell
cd apps/web-client
npm run build
```

### Step 8. 커밋

문제 없으면:

```powershell
git add .
git commit -m "commit message suggested by Codex"
```

### Step 9. push

```powershell
git push
```

---

## 4. Safe Codex Review Prompt

Codex가 작업을 끝낸 뒤, 바로 커밋하지 말고 아래 프롬프트를 넣습니다.

```text
Review the current diff using CODE_REVIEW.md.

Summarize:
1. changed files
2. main behavior added
3. checks run
4. possible risks

Then suggest a commit message.

Do not push to GitHub.
```

---

## 5. Emergency Rollback

Codex가 이상하게 수정했을 때:

### 변경 사항만 버리기

```powershell
git restore .
```

### 새로 생긴 파일까지 제거하기

주의: 새로 만든 파일도 지워집니다.

```powershell
git clean -fd
```

### 가장 최근 커밋으로 되돌리기

```powershell
git reset --hard HEAD
```

---

## 6. Important Rules

- Codex에게 한 번에 전체 프로젝트를 만들라고 하지 않습니다.
- 반드시 Phase 하나씩 진행합니다.
- push는 사용자가 직접 합니다.
- 실제 API key는 절대 저장소에 올리지 않습니다.
- `.env`는 커밋하지 않습니다.
- `.env.example`만 커밋합니다.
- 성능 결과는 실제로 측정한 값만 적습니다.
- README에는 현재 구현된 내용과 앞으로 할 내용을 구분합니다.

---

## 7. Recommended Git Commands

작업 전:

```powershell
git status
git pull
```

작업 후:

```powershell
git status
git diff --stat
git add .
git commit -m "message"
git push
```

로그 확인:

```powershell
git log --oneline --max-count=5
```
