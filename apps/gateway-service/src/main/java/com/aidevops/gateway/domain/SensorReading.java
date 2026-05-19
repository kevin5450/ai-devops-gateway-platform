package com.aidevops.gateway.domain;

import java.time.LocalDateTime;

public record SensorReading(
        DeviceId deviceId,
        LocalDateTime measuredAt,
        double temperature,
        double humidity,
        double light
) {
}
