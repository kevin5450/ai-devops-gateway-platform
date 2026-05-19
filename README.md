# ai-devops-gateway-platform

React, Spring Boot, AI Service, Redis, Kafka, MongoDB를 조합해 DevOps 관점의 AI Gateway 플랫폼을 구성하는 monorepo 프로젝트입니다.

현재 단계에서는 실제 애플리케이션 코드를 만들지 않고, 프로젝트 목표와 예정 아키텍처, 디렉터리 구조만 정의합니다.

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
- 실제 Spring Boot, React, Kafka, Redis, MongoDB 코드는 아직 없음
