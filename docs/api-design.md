# API Design

이 문서는 Forest IoT Monitoring Backend의 초기 API 설계를 정리합니다.

---

## 1. API Design Goal

초기 API의 목적은 다음과 같습니다.

```text
1. 서버가 정상 동작하는지 확인한다.
2. 장비가 보낸 센서 데이터를 수집한다.
3. 장비별 최신 센서 데이터를 조회한다.
4. 장비별 최신 이상 상태를 조회한다.
```

Phase 3에서는 DB 연결 없이 in-memory 저장소를 사용합니다.

---

## 2. Health Check API

### Endpoint

```http
GET /api/health
```

### Response

```json
{
  "status": "UP",
  "service": "gateway-service"
}
```

### Purpose

서버 실행 여부를 빠르게 확인하기 위한 API입니다.

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

### Validation Rules

| Field | Rule |
|---|---|
| deviceId | required |
| measuredAt | required |
| temperature | required |
| humidity | required, 0~100 |
| light | required, 0 이상 |

### Error Response Example

```json
{
  "code": "INVALID_REQUEST",
  "message": "humidity must be between 0 and 100"
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
  "light": 832.5
}
```

### No Data Response

초기 구현에서는 404 응답을 권장합니다.

```json
{
  "code": "NOT_FOUND",
  "message": "No sensor reading found for deviceId: Cube1"
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
    "message": "현재 이슈 없음"
  },
  "humidity": {
    "status": "OK",
    "message": "현재 이슈 없음"
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
    "message": "적정 온도 범위 초과: 27℃ 초과"
  },
  "humidity": {
    "status": "OK",
    "message": "현재 이슈 없음"
  }
}
```

---

## 6. Threshold Policy

초기 기준값은 기존 forest 프로젝트의 값을 참고합니다.

```text
TEMP_MIN = 18
TEMP_MAX = 27
HUM_MIN = 40
HUM_MAX = 75
```

정책 로직은 Controller에 넣지 않고 `SensorThresholdPolicy` 같은 별도 클래스로 분리합니다.

---

## 7. API Evolution Plan

향후 API는 다음과 같이 확장합니다.

| Phase | API | Purpose |
|---|---|---|
| Phase 3 | `/api/readings` | in-memory 센서 데이터 수집 |
| Phase 3 | `/api/devices/{deviceId}/latest` | 최신값 조회 |
| Phase 3 | `/api/devices/{deviceId}/issues/latest` | 최신 이슈 조회 |
| Phase 5 | same APIs | MongoDB 저장소로 변경 |
| Phase 6 | `/api/devices/{deviceId}/daily-summary` | 일별 요약 조회 |
| Phase 8 | `/api/metrics/latency` | 지연 시간 측정 결과 조회 |
