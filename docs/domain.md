# Domain Model: Forest IoT Monitoring Backend

이 문서는 Forest IoT Monitoring Backend의 도메인 개념을 정의합니다.

---

## 1. Domain Summary

이 프로젝트는 현장 장비에서 발생하는 센서 데이터를 백엔드 서버가 수집하고, 데이터의 형식과 값의 범위를 검증한 뒤, 장비별 최신 상태와 이상 상태를 조회할 수 있도록 만드는 시스템입니다.

도메인의 핵심 질문은 다음과 같습니다.

```text
어떤 장비가 데이터를 보냈는가?
언제 측정한 데이터인가?
온도, 습도, 조도 값은 정상 범위인가?
가장 최근 데이터는 무엇인가?
현재 또는 최근에 발생한 이상 상태는 무엇인가?
```

---

## 2. Core Concepts

## 2.1 Device

Device는 현장에서 센서 데이터를 전송하는 장비입니다.

예시:

```text
Cube1
Cube2
Cube3
```

기존 forest 프로젝트에서도 Cube 단위로 데이터를 구분했습니다. 새 Spring Boot 프로젝트에서도 `deviceId`를 중심으로 데이터를 관리합니다.

### Fields

| Field | Type | Description |
|---|---|---|
| deviceId | String | 장비 식별자. 예: Cube1 |

### Rule

초기에는 `Cube숫자` 형식을 권장합니다.

```text
Cube1: valid
Cube200: valid
SensorA: not recommended
empty string: invalid
```

---

## 2.2 SensorReading

SensorReading은 장비가 특정 시점에 전송한 센서 측정값입니다.

### Fields

| Field | Type | Description |
|---|---|---|
| deviceId | String | 데이터를 보낸 장비 ID |
| measuredAt | LocalDateTime 또는 Instant | 실제 측정 시각 |
| temperature | Double | 온도 값 |
| humidity | Double | 습도 값 |
| light | Double | 조도 값 |

### Example

```json
{
  "deviceId": "Cube1",
  "measuredAt": "2026-05-19T15:30:00",
  "temperature": 24.5,
  "humidity": 61.2,
  "light": 832.5
}
```

---

## 2.3 SensorIssue

SensorIssue는 센서 값이 기준 범위를 벗어났을 때 생성되는 이상 상태입니다.

기존 forest 프로젝트에서는 온도와 습도에 대해 다음 기준을 사용했습니다.

```text
TEMP_MIN = 18
TEMP_MAX = 27
HUM_MIN = 40
HUM_MAX = 75
```

새 프로젝트에서도 초기 기준은 이 값을 사용합니다.

### Temperature Issue Rule

| Condition | Status | Message |
|---|---|---|
| temperature < 18 | LOW | 적정 온도 범위 미만 |
| temperature > 27 | HIGH | 적정 온도 범위 초과 |
| otherwise | OK | 현재 이슈 없음 |

### Humidity Issue Rule

| Condition | Status | Message |
|---|---|---|
| humidity < 40 | LOW | 적정 습도 범위 미만 |
| humidity > 75 | HIGH | 적정 습도 범위 초과 |
| otherwise | OK | 현재 이슈 없음 |

---

## 2.4 DeviceStatus

DeviceStatus는 장비의 현재 상태를 나타냅니다.

초기에는 복잡한 상태 모델을 만들지 않고, 최신 센서 데이터와 SensorIssue 결과를 바탕으로 간단히 판단합니다.

| Status | Meaning |
|---|---|
| NORMAL | 최신 데이터가 있고 이슈가 없음 |
| WARNING | 온도 또는 습도 중 하나가 기준 범위를 벗어남 |
| NO_DATA | 해당 장비의 데이터가 없음 |

---

## 2.5 DailySummary

DailySummary는 하루 단위로 센서 데이터를 집계한 결과입니다.

기존 forest 프로젝트에는 `/api/daily/summary` API가 존재했습니다. 새 프로젝트에서는 Phase 6 이후 구현합니다.

### Fields

| Field | Type | Description |
|---|---|---|
| deviceId | String | 장비 ID |
| date | LocalDate | 집계 날짜 |
| avgTemperature | Double | 평균 온도 |
| avgHumidity | Double | 평균 습도 |
| avgLight | Double | 평균 조도 |
| samples | Long | 집계에 사용된 샘플 수 |
| comment | String | 요약 코멘트 |

---

## 3. Domain Flow

초기 시스템 흐름은 다음과 같습니다.

```text
POST /api/readings
→ request validation
→ SensorReading 생성
→ SensorThresholdPolicy로 이상 여부 판단
→ InMemorySensorReadingStore에 최신값 저장
→ 응답 반환
```

조회 흐름은 다음과 같습니다.

```text
GET /api/devices/{deviceId}/latest
→ deviceId 기준 최신 SensorReading 조회
→ 있으면 JSON 반환
→ 없으면 404 또는 NO_DATA 응답
```

이슈 조회 흐름은 다음과 같습니다.

```text
GET /api/devices/{deviceId}/issues/latest
→ deviceId 기준 최신 SensorReading 조회
→ SensorThresholdPolicy 적용
→ 온도/습도 이슈 상태 반환
```

---

## 4. Why This Domain Matters

현장 데이터는 단순히 DB에 저장되는 값이 아닙니다.

센서 데이터는 다음 문제를 가질 수 있습니다.

```text
장비 ID 누락
측정 시각 누락
잘못된 숫자 값
비정상적인 습도 범위
통신 지연으로 인한 오래된 데이터
장비별 최신 상태 불일치
```

따라서 이 프로젝트의 핵심은 단순 CRUD가 아니라, 현장 데이터가 신뢰 가능한 상태로 서버에 들어오고 조회될 수 있게 만드는 것입니다.

---

## 5. Future Extension

향후 확장 방향은 다음과 같습니다.

```text
MongoDB 저장소 연동
일별 요약 통계
장비별 데이터 수집 지연 감지
P95/P99/P99.9 지연 시간 측정
Docker 기반 실행 환경 구성
GitHub Actions 기반 빌드 자동화
클라우드 배포
```
