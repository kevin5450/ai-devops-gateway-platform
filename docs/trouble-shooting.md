# Trouble Shooting

This document records common local development issues and fixes.

## Gateway Build

Run the build from the gateway service directory:

```powershell
cd apps/gateway-service
.\gradlew.bat build
```

If PowerShell cannot find the command, confirm that the current directory contains `gradlew.bat`.

## Gateway Tests

Run only tests with:

```powershell
cd apps/gateway-service
.\gradlew.bat test
```

Test reports are written under:

```text
apps/gateway-service/build/reports/tests/test/index.html
```

## Local Run

Run the gateway service with:

```powershell
cd apps/gateway-service
.\gradlew.bat bootRun
```

The default port is `8080`.

Quick health check:

```powershell
curl http://localhost:8080/api/health
```

## API Validation Errors

`POST /api/readings` returns `400 VALIDATION_ERROR` when:

- `deviceId` is missing or blank
- `measuredAt` is missing
- `temperature` is missing
- `humidity` is missing, below 0, or above 100
- `light` is missing or negative

## Missing Device Data

`GET /api/devices/{deviceId}/latest` and `GET /api/devices/{deviceId}/issues/latest` return `404 NOT_FOUND` when no reading has been submitted for that device.

## In-Memory Storage Limitation

Current readings are stored in memory only. Restarting the gateway clears submitted readings. This is intentional until MongoDB is added in a later phase.
