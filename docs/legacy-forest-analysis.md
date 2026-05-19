# Legacy Forest Project Analysis

이 문서는 기존 Node.js 기반 forest 프로젝트에서 새 Spring Boot 프로젝트로 가져올 개념과 가져오지 않을 요소를 정리합니다.

---

## 1. Legacy Project Summary

기존 forest 프로젝트는 Node.js, Express, MongoDB를 기반으로 구성되어 있었습니다.

핵심 기능은 다음과 같습니다.

```text
1. MongoDB에서 Cube 장비 컬렉션 탐색
2. Cube별 최신 센서 데이터 조회
3. 온도/습도 기준 기반 현재 또는 최근 이슈 조회
4. 일별 평균 온도/습도/조도 요약
5. 웹 화면에서 장비 상태와 이슈 표시
```

---

## 2. Important Legacy Files

| File | Role |
|---|---|
| server.js | Express 서버, MongoDB 연결, REST API 제공 |
| event.js | 웹 화면에서 daily summary와 issue API 호출 |
| graph.js | 시각화 관련 클라이언트 코드 |
| package.json | Node.js 의존성 및 실행 스크립트 |
| DB_exp/ | DB 성능 실험 및 테스트 코드 |
| index.html, forestsubpage.html | 기존 프론트엔드 화면 |

---

## 3. Legacy API Mapping

기존 API를 새 Spring Boot API로 다음과 같이 재설계합니다.

| Legacy API | New API | Note |
|---|---|---|
| `GET /api/latest?dev=Cube1` | `GET /api/devices/{deviceId}/latest` | query parameter 대신 path variable 사용 |
| `GET /api/latest?dev=all` | later: `GET /api/devices/latest` | 전체 최신값 조회는 후속 Phase에서 구현 |
| `GET /api/issues/latest?dev=Cube1` | `GET /api/devices/{deviceId}/issues/latest` | 장비 기준 이슈 조회 |
| `GET /api/daily/summary?dev=Cube1&date=YYYY-MM-DD` | later: `GET /api/devices/{deviceId}/daily-summary?date=YYYY-MM-DD` | 일별 통계는 Phase 6에서 구현 |

---

## 4. Legacy Logic to Keep

새 프로젝트에 반영할 핵심 로직은 다음과 같습니다.

### 4.1 Device Naming

기존 프로젝트는 `Cube1`, `Cube2` 같은 장비 이름을 사용했습니다.

새 프로젝트에서도 `deviceId`는 `Cube숫자` 형식을 우선 사용합니다.

### 4.2 Sensor Fields

기존 프로젝트는 다양한 필드명을 처리했습니다.

```text
temp 또는 temperature
humidity
max_lux 또는 lux_max 또는 lux 또는 illuminance
timestamp 또는 timestamp_str
```

새 프로젝트의 API에서는 외부 입력 형식을 먼저 명확히 제한합니다.

```text
temperature
humidity
light
measuredAt
```

기존 필드명 호환은 Phase 후반에 adapter 형태로 고려합니다.

### 4.3 Thresholds

기존 프로젝트 기준값:

```text
TEMP_MIN = 18
TEMP_MAX = 27
HUM_MIN = 40
HUM_MAX = 75
```

새 프로젝트에서도 초기 기준값으로 사용하되, 코드에서는 `SensorThresholdPolicy`로 분리합니다.

### 4.4 Latest Data Cache

기존 프로젝트는 `latestByCube` Map을 사용하여 장비별 최신 데이터를 보관했습니다.

새 프로젝트 Phase 3에서는 같은 아이디어를 Spring Boot의 in-memory store로 구현합니다.

```text
Map<DeviceId, SensorReading>
```

이후 Phase 5에서 MongoDB 저장소로 확장합니다.

---

## 5. Legacy Logic Not to Copy Directly

다음 항목은 새 프로젝트에 그대로 복사하지 않습니다.

```text
Node.js Express 서버 구조
기존 frontend HTML/CSS/JS 전체
node_modules
.env
.git
3D model 파일
이미지 파일
임시 테스트 코드
```

이 프로젝트의 목적은 기존 코드를 복사하는 것이 아니라 Spring Boot 방식으로 재설계하는 것입니다.

---

## 6. Design Improvements in New Project

기존 forest 프로젝트의 기능을 유지하되, 새 프로젝트에서는 다음을 개선합니다.

| Area | Legacy | New Project |
|---|---|---|
| Backend Framework | Express | Spring Boot |
| Structure | server.js 중심 | Controller-Service-Domain 계층 분리 |
| Validation | 함수 내부 처리 | DTO validation + 정책 클래스 |
| Latest Cache | Map 직접 사용 | InMemorySensorReadingStore로 분리 |
| Threshold Logic | API 내부 함수 | SensorThresholdPolicy로 분리 |
| DB | MongoDB 직접 접근 | Repository 계층으로 분리 예정 |
| API Style | query parameter 중심 | REST path 중심 |
| Build | npm start | Gradle wrapper 기반 build |

---

## 7. Portfolio Explanation

면접 또는 README에서 이 프로젝트는 다음처럼 설명할 수 있습니다.

```text
기존에는 Node.js와 MongoDB 기반으로 현장 센서 데이터를 수집하고 최신 상태를 웹에서 확인하는 프로젝트를 구현했습니다.
이번에는 같은 문제를 Java/Spring Boot 기반으로 재설계하면서, API 계층 분리, 데이터 검증, 이상 상태 판단 로직 분리, 빌드 재현성까지 고려한 백엔드 구조로 확장하고 있습니다.
```

---

## 8. Next Step

Phase 3에서는 기존 forest의 모든 기능을 옮기지 않습니다.

우선 다음만 구현합니다.

```text
GET /api/health
POST /api/readings
GET /api/devices/{deviceId}/latest
GET /api/devices/{deviceId}/issues/latest
```

DB 연결과 일별 요약은 후속 Phase에서 진행합니다.
