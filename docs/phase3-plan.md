# Phase 3 Plan: Define IoT Monitoring Domain and Base API

이 문서는 Phase 3 진행 계획을 정리합니다.

---

## 1. Phase 3 Goal

Phase 3의 목표는 Spring Boot gateway-service 안에 Forest IoT Monitoring Backend의 기본 도메인과 API 구조를 구현하는 것입니다.

아직 MongoDB는 연결하지 않습니다.

먼저 in-memory 저장 방식으로 다음을 검증합니다.

```text
1. 센서 데이터 요청을 받을 수 있는가?
2. 요청 값을 검증할 수 있는가?
3. 장비별 최신 데이터를 저장할 수 있는가?
4. 장비별 최신 데이터를 조회할 수 있는가?
5. 온도/습도 기준으로 이상 상태를 판단할 수 있는가?
```

---

## 2. Why In-Memory First?

처음부터 MongoDB를 붙이면 다음 개념이 한꺼번에 섞입니다.

```text
API 설계
DTO 설계
도메인 객체 설계
검증 로직
예외 처리
DB 연결
MongoDB Document 설계
Repository 설계
환경변수 관리
```

학습과 포트폴리오 관점에서는 먼저 도메인과 API를 안정화한 뒤 DB를 붙이는 것이 더 좋습니다.

---

## 3. Required API

### 3.1 Health API

```http
GET /api/health
```

서버 실행 여부를 확인합니다.

### 3.2 Sensor Reading Create API

```http
POST /api/readings
```

장비가 보낸 센서 데이터를 수집합니다.

### 3.3 Latest Reading API

```http
GET /api/devices/{deviceId}/latest
```

특정 장비의 최신 센서 데이터를 조회합니다.

### 3.4 Latest Issue API

```http
GET /api/devices/{deviceId}/issues/latest
```

특정 장비의 최신 센서 데이터 기준으로 이상 상태를 조회합니다.

---

## 4. Suggested Implementation Order

Phase 3는 다음 순서로 진행합니다.

```text
1. health 패키지와 HealthController 생성
2. reading 도메인 패키지 생성
3. SensorReadingRequest DTO 생성
4. SensorReading domain 객체 생성
5. InMemorySensorReadingStore 생성
6. SensorThresholdPolicy 생성
7. SensorReadingService 생성
8. SensorReadingController 생성
9. ErrorResponse와 GlobalExceptionHandler 생성
10. README와 docs 업데이트
11. Gradle build 실행
```

---

## 5. Suggested Classes

```text
HealthController
SensorReadingController
SensorReadingService
SensorReadingRequest
SensorReadingResponse
SensorIssueResponse
DeviceId
SensorReading
SensorIssue
IssueStatus
SensorThresholdPolicy
InMemorySensorReadingStore
ErrorResponse
GlobalExceptionHandler
```

---

## 6. Validation Detail

초기 검증 규칙은 다음과 같습니다.

```text
deviceId: null 또는 blank 불가
measuredAt: null 불가
temperature: null 불가
humidity: null 불가, 0 이상 100 이하
light: null 불가, 0 이상
```

Device ID 형식은 초기에는 강하게 막지 않고, 경고성 문서화만 해도 됩니다.
하지만 코드 구조상 나중에 `Cube숫자` 형식을 강제할 수 있게 `DeviceId` 객체를 분리하는 것이 좋습니다.

---

## 7. Test Scenarios

Phase 3 구현 후 수동으로 확인할 시나리오는 다음과 같습니다.

### 7.1 Health Check

```bash
curl http://localhost:8080/api/health
```

Expected:

```json
{
  "status": "UP",
  "service": "gateway-service"
}
```

### 7.2 Create Reading

```bash
curl -X POST http://localhost:8080/api/readings ^
  -H "Content-Type: application/json" ^
  -d "{\"deviceId\":\"Cube1\",\"measuredAt\":\"2026-05-19T15:30:00\",\"temperature\":24.5,\"humidity\":61.2,\"light\":832.5}"
```

### 7.3 Get Latest

```bash
curl http://localhost:8080/api/devices/Cube1/latest
```

### 7.4 Get Latest Issue

```bash
curl http://localhost:8080/api/devices/Cube1/issues/latest
```

### 7.5 Invalid Humidity

```bash
curl -X POST http://localhost:8080/api/readings ^
  -H "Content-Type: application/json" ^
  -d "{\"deviceId\":\"Cube1\",\"measuredAt\":\"2026-05-19T15:30:00\",\"temperature\":24.5,\"humidity\":120,\"light\":832.5}"
```

Expected: 400 error

---

## 8. Completion Criteria

Phase 3 완료 조건은 다음과 같습니다.

```text
GET /api/health 동작
POST /api/readings 동작
GET /api/devices/{deviceId}/latest 동작
GET /api/devices/{deviceId}/issues/latest 동작
잘못된 요청에 대해 오류 응답 반환
README와 docs가 현재 도메인 반영
.\gradlew.bat build 통과
```

---

## 9. Commit Message

Phase 3 완료 후 권장 커밋 메시지는 다음과 같습니다.

```text
add sensor reading base api
```
