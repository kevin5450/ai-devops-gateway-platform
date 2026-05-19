# ai-devops-gateway-platform

Spring Boot Gateway, React Web Client, AI Service, Log Service를 중심으로 구성하는 DevOps 학습용 AI Gateway 플랫폼입니다.

아직 실제 서비스 코드는 작성하지 않았으며, 이 README는 프로젝트 목표와 예정 아키텍처를 간단히 정리합니다.

## Project Goals

- Spring Boot 기반 Gateway Service 구축
- React 기반 Web Client 구축
- Node.js 또는 Python 기반 AI Service 구축
- Spring Boot 기반 Log Service 구축
- Redis 기반 캐시 및 Rate Limit 구성
- Kafka 기반 비동기 이벤트 처리 구성
- MongoDB 기반 로그 저장 구성
- Docker Compose 기반 로컬 실행 환경 구성
- Kubernetes manifest 기반 배포 구조 구성
- GitHub Actions 기반 CI/CD 구성
- k6 기반 부하테스트 구성

## Planned Architecture

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
        +--> Kafka (Async Events)
                 |
                 v
          Log Service (Spring Boot)
                 |
                 v
              MongoDB
```

## Planned Components

### Gateway Service

- 클라이언트 요청 진입점
- 인증, 라우팅, Rate Limit, 캐시 연동 담당
- AI Service 호출 및 이벤트 발행 담당

### Web Client

- React 기반 사용자 화면
- Gateway Service API 호출

### AI Service

- Node.js 또는 Python 기반 AI 처리 서비스
- Gateway Service로부터 요청을 받아 AI 응답 생성

### Log Service

- Spring Boot 기반 로그 처리 서비스
- Kafka 이벤트를 소비하여 MongoDB에 로그 저장

### Infrastructure

- Redis: 캐시 및 Rate Limit 저장소
- Kafka: 서비스 간 비동기 이벤트 처리
- MongoDB: 요청/응답 및 이벤트 로그 저장
- Docker Compose: 로컬 개발 및 테스트 실행
- Kubernetes Manifests: 배포 리소스 정의
- GitHub Actions: CI/CD 파이프라인
- k6: 부하테스트 시나리오

## Current Status

- 프로젝트 초기 기획 단계
- 실제 애플리케이션 코드는 아직 없음
- README에 목표와 예정 아키텍처만 정리
