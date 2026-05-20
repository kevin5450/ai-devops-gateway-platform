package com.aidevops.gateway.persistence.mongo;

import com.aidevops.gateway.domain.DeviceId;
import com.aidevops.gateway.domain.SensorReading;
import java.time.Instant;

public final class SensorReadingMapper {

    private SensorReadingMapper() {
    }

    public static SensorReadingDocument toDocument(SensorReading reading) {
        return new SensorReadingDocument(
                reading.deviceId().value(),
                reading.measuredAt(),
                reading.temperature(),
                reading.humidity(),
                reading.light(),
                Instant.now()
        );
    }

    public static SensorReading toDomain(SensorReadingDocument document) {
        return new SensorReading(
                new DeviceId(document.getDeviceId()),
                document.getMeasuredAt(),
                document.getTemperature(),
                document.getHumidity(),
                document.getLight()
        );
    }
}
