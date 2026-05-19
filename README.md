# ai-devops-gateway-platform

AI DevOps Gateway Platform is a planned platform for building, operating, and testing an AI-enabled gateway system with supporting web, logging, messaging, cache, and deployment infrastructure.

## Project Goals

- Build a Spring Boot based Gateway Service.
- Build a React based Web Client.
- Build an AI Service using Node.js or Python.
- Build a Spring Boot based Log Service.
- Use Redis for caching and rate limiting.
- Use Kafka for asynchronous event processing.
- Use MongoDB for log storage.
- Support local execution with Docker Compose.
- Provide Kubernetes manifests for deployment.
- Configure CI/CD with GitHub Actions.
- Add load testing with k6.

## Planned Architecture

```text
React Web Client
        |
        v
Spring Boot Gateway Service
        |
        +--> AI Service (Node.js or Python)
        |
        +--> Redis (Cache / Rate Limit)
        |
        +--> Kafka (Async Events)
                  |
                  v
        Spring Boot Log Service
                  |
                  v
              MongoDB
```

## Planned Infrastructure

- Docker Compose for local development and integration testing.
- Kubernetes manifests for service deployment.
- GitHub Actions for build, test, and deployment workflows.
- k6 scripts for gateway and service load testing.

## Current Status

This repository currently contains only the initial project README. Application source code and infrastructure files will be added later.
