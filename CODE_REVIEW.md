# CODE_REVIEW.md

이 파일은 Codex가 작업 후 스스로 점검할 기준입니다.

Codex는 커밋을 제안하기 전에 이 기준으로 변경 내용을 확인해야 합니다.

---

## 1. General Review

- [ ] 요청한 Phase만 작업했는가?
- [ ] 불필요하게 전체 구조를 갈아엎지 않았는가?
- [ ] 기존 파일을 이유 없이 삭제하지 않았는가?
- [ ] 변경 범위가 작고 리뷰 가능한가?
- [ ] 실제 비밀키나 `.env` 파일을 커밋하지 않았는가?
- [ ] `.env.example`만 추가했는가?
- [ ] README 또는 문서가 실제 코드 상태와 맞는가?

---

## 2. Spring Boot Review

- [ ] Gradle을 사용하는가?
- [ ] Java 21 기준인가?
- [ ] `/health` endpoint가 있는가?
- [ ] Controller, Service, DTO, Config가 적절히 분리되어 있는가?
- [ ] 예외 응답 구조가 일관적인가?
- [ ] 환경변수로 외부 서비스 주소를 설정할 수 있는가?
- [ ] 빌드 명령이 성공하는가?

Recommended check:

```powershell
cd apps/gateway-service
./gradlew build
```

Windows에서 필요하면:

```powershell
cd apps/gateway-service
gradlew.bat build
```

---

## 3. React Review

- [ ] Gateway API를 호출하는가?
- [ ] loading 상태가 있는가?
- [ ] error 상태가 있는가?
- [ ] 성공 응답을 화면에 보여주는가?
- [ ] UI가 과하게 복잡하지 않은가?

Recommended check:

```powershell
cd apps/web-client
npm install
npm run build
```

---

## 4. AI Service Review

- [ ] `/health` endpoint가 있는가?
- [ ] `/ai/chat` endpoint가 있는가?
- [ ] mock response가 동작하는가?
- [ ] API key를 하드코딩하지 않았는가?
- [ ] 환경변수 구조가 있는가?

---

## 5. Redis Review

- [ ] Redis 연결 정보가 환경변수화되어 있는가?
- [ ] Rate limit 기준이 명확한가?
- [ ] 제한 초과 시 명확한 응답을 반환하는가?
- [ ] README에 rate limit 규칙이 설명되어 있는가?

---

## 6. Kafka Review

- [ ] Topic 이름이 문서화되어 있는가?
- [ ] Event schema가 문서화되어 있는가?
- [ ] Producer가 Gateway에 있는가?
- [ ] Consumer가 Log Service에 있는가?
- [ ] Kafka 장애가 전체 API 장애로 바로 이어지지 않도록 고려했는가?

---

## 7. MongoDB Review

- [ ] request log document 구조가 명확한가?
- [ ] requestId가 저장되는가?
- [ ] createdAt이 저장되는가?
- [ ] latencyMs가 저장되는가?
- [ ] `/logs/recent` endpoint가 실제 저장 데이터와 맞는가?

---

## 8. Docker Review

- [ ] 각 앱의 Dockerfile이 있는가?
- [ ] docker-compose에 필요한 서비스가 포함되어 있는가?
- [ ] 포트가 문서화되어 있는가?
- [ ] 환경변수가 분리되어 있는가?
- [ ] `docker compose config`가 통과하는가?

Recommended check:

```powershell
docker compose -f infra/docker/docker-compose.yml config
```

---

## 9. Kubernetes Review

- [ ] manifest가 `infra/k8s` 아래에 있는가?
- [ ] namespace가 있는가?
- [ ] 각 app의 deployment/service가 분리되어 있는가?
- [ ] secret이 직접 하드코딩되어 있지 않은가?
- [ ] README에 적용 순서가 설명되어 있는가?

---

## 10. Performance Review

- [ ] k6 script가 `/api/chat`을 테스트하는가?
- [ ] avg, p95, p99를 확인할 수 있는가?
- [ ] 실제 측정하지 않은 결과를 꾸며내지 않았는가?
- [ ] 병목과 개선 방향을 문서화했는가?

---

## 11. Final Output Format

After each task, Codex should summarize:

```text
Changed files:
- ...

What changed:
- ...

Checks:
- ...

Risks or notes:
- ...

Suggested commit:
- ...
```
