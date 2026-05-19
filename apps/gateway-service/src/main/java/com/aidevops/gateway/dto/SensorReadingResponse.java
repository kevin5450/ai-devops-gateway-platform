package com.aidevops.gateway.dto;

import com.aidevops.gateway.domain.SensorReading;
import java.time.LocalDateTime;

public record SensorReadingResponse(
        String deviceId,
        LocalDateTime measuredAt,
        double temperature,
        double humidity,
        double light,
        String message
) {

    public static SensorReadingResponse accepted(SensorReading reading) {
        return from(reading, "Sensor reading accepted");
    }

    public static SensorReadingResponse latest(SensorReading reading) {
        return from(reading, null);
    }

    private static SensorReadingResponse from(SensorReading reading, String message) {
        return new SensorReadingResponse(
                reading.deviceId().value(),
                reading.measuredAt(),
                reading.temperature(),
                reading.humidity(),
                reading.light(),
                message
        );
    }
}
