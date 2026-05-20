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

The service keeps the repository boundary explicit:

```text
SensorReadingRepository
      |
      +-- InMemorySensorReadingStore      default profile
      |
      +-- MongoSensorReadingRepository    mongo profile
```

The controller and service layers depend only on `SensorReadingRepository`. The active storage implementation is selected by Spring profile.

MongoDB connection values are read from environment-backed Spring properties:

```yaml
spring:
  data:
    mongodb:
      uri: ${MONGODB_URI:mongodb://localhost:27017/forest_iot_gateway}
```

No secrets are hardcoded in the application.

## Planned Expansion

- React Web Client
- Expanded MongoDB query APIs for readings and issue history
- DailySummary API
- DeviceStatus API
- Redis rate limiting
- Kafka event flow
- Spring Boot Log Service
- Docker Compose
- Kubernetes manifests
- GitHub Actions
- k6 load tests
