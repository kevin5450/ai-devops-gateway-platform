# Architecture

This document describes the current architecture direction for `ai-devops-gateway-platform`.

The domain is Forest IoT Monitoring Backend: field devices send sensor readings, the gateway validates them, stores the latest in-memory state, and exposes monitoring APIs.

## Current Implemented Scope

- Spring Boot Gateway Service
- Controller-Service-DTO-Domain-Repository package structure
- In-memory latest reading storage
- Sensor validation
- Temperature and humidity issue policy
- API and policy tests

## Current Runtime Flow

```text
Device / client
      |
      v
POST /api/readings
      |
      v
SensorReadingController
      |
      v
SensorReadingService
      |
      +--> SensorThresholdPolicy
      |
      v
SensorReadingRepository
      |
      v
InMemorySensorReadingStore
```

Read APIs:

```text
GET /api/devices/{deviceId}/latest
GET /api/devices/{deviceId}/issues/latest
```

## Persistence Boundary

MongoDB is intentionally not connected yet. Phase 5 prepares for persistence by using a repository interface:

```text
SensorReadingRepository
      ^
      |
InMemorySensorReadingStore
```

A future MongoDB repository can implement the same interface after the API contract and validation behavior are stable.

## Planned Expansion

- React Web Client
- MongoDB persistence for readings and issue history
- DailySummary API
- DeviceStatus API
- Redis rate limiting
- Kafka event flow
- Spring Boot Log Service
- Docker Compose
- Kubernetes manifests
- GitHub Actions
- k6 load tests
