# API Design

This document describes the current Forest IoT Monitoring Backend API.

The service is a Spring Boot reimplementation and extension of a previous Node.js Forest IoT monitoring project. Legacy code is not copied into this repository. The current implementation focuses on field IoT sensor data ingestion, validation, and monitoring.

---

## 1. API Design Goal

The current API answers four questions:

```text
1. Is the gateway service running?
2. Can a device submit sensor readings?
3. What is the latest reading for a device?
4. Does the latest reading indicate a temperature or humidity issue?
```

Phases 3 through 5 intentionally use in-memory storage. MongoDB integration is postponed so that the API contract, validation behavior, domain model, and service boundaries can be stabilized first.

---

## 2. Health Check API

### Endpoint

```http
GET /api/health
```

`GET /health` is also kept for the earlier gateway health check.

### Response

```json
{
  "status": "UP",
  "service": "gateway-service",
  "timestamp": "2026-05-19T07:00:00Z"
}
```

### curl

```bash
curl http://localhost:8080/api/health
```

---

## 3. Create Sensor Reading API

### Endpoint

```http
POST /api/readings
```

### Request Body

```json
{
  "deviceId": "Cube1",
  "measuredAt": "2026-05-19T15:30:00",
  "temperature": 24.5,
  "humidity": 61.2,
  "light": 832.5
}
```

### Success Response

```json
{
  "deviceId": "Cube1",
  "measuredAt": "2026-05-19T15:30:00",
  "temperature": 24.5,
  "humidity": 61.2,
  "light": 832.5,
  "message": "Sensor reading accepted"
}
```

### curl

```bash
curl -X POST http://localhost:8080/api/readings \
  -H "Content-Type: application/json" \
  -d "{\"deviceId\":\"Cube1\",\"measuredAt\":\"2026-05-19T15:30:00\",\"temperature\":24.5,\"humidity\":61.2,\"light\":832.5}"
```

### Validation Rules

| Field | Rule |
|---|---|
| `deviceId` | Required, not blank |
| `measuredAt` | Required, ISO-8601 local date-time |
| `temperature` | Required |
| `humidity` | Required, 0 to 100 |
| `light` | Required, greater than or equal to 0 |

### Error Response Example

```json
{
  "code": "VALIDATION_ERROR",
  "message": "humidity: humidity must be between 0 and 100",
  "path": "/api/readings",
  "timestamp": "2026-05-19T07:00:00Z"
}
```

---

## 4. Get Latest Sensor Reading API

### Endpoint

```http
GET /api/devices/{deviceId}/latest
```

### Example

```http
GET /api/devices/Cube1/latest
```

### Success Response

```json
{
  "deviceId": "Cube1",
  "measuredAt": "2026-05-19T15:30:00",
  "temperature": 24.5,
  "humidity": 61.2,
  "light": 832.5,
  "message": null
}
```

### No Data Response

```json
{
  "code": "NOT_FOUND",
  "message": "No sensor reading found for deviceId: Cube1",
  "path": "/api/devices/Cube1/latest",
  "timestamp": "2026-05-19T07:00:00Z"
}
```

---

## 5. Get Latest Sensor Issue API

### Endpoint

```http
GET /api/devices/{deviceId}/issues/latest
```

### Example

```http
GET /api/devices/Cube1/issues/latest
```

### Success Response Example: Normal

```json
{
  "deviceId": "Cube1",
  "measuredAt": "2026-05-19T15:30:00",
  "temperature": {
    "status": "OK",
    "message": "No current issue"
  },
  "humidity": {
    "status": "OK",
    "message": "No current issue"
  }
}
```

### Success Response Example: Abnormal

```json
{
  "deviceId": "Cube1",
  "measuredAt": "2026-05-19T15:30:00",
  "temperature": {
    "status": "HIGH",
    "message": "Temperature is above safe range: above 27"
  },
  "humidity": {
    "status": "LOW",
    "message": "Humidity is below safe range: below 40"
  }
}
```

---

## 6. Threshold Policy

Current issue detection uses these initial thresholds:

| Metric | LOW | OK | HIGH |
|---|---|---|---|
| temperature | `< 18` | `18~27` | `> 27` |
| humidity | `< 40` | `40~75` | `> 75` |

The policy lives in `SensorThresholdPolicy` instead of the controller so it can be tested and changed independently.

---

## 7. Current Persistence Decision

The current implementation stores only the latest reading per device in memory.

MongoDB is intentionally postponed because Phase 5 is about stabilizing:

- API request and response shape
- validation behavior
- domain records
- service and policy boundaries
- repository abstraction for future persistence

The code already has a `SensorReadingRepository` interface. The current implementation is `InMemorySensorReadingStore`; a later MongoDB implementation can replace it without changing the controller contract.

---

## 8. Next API Evolution

| Future Area | Direction |
|---|---|
| MongoDB | Persist readings and issue history |
| DailySummary | Add daily aggregation per device |
| DeviceStatus | Add `NORMAL`, `WARNING`, `NO_DATA` summary status |
| Operations | Add Redis, Kafka, Docker, Kubernetes, CI, and k6 in later phases |
