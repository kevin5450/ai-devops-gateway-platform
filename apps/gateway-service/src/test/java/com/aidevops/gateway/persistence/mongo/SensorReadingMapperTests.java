package com.aidevops.gateway.persistence.mongo;

import static org.assertj.core.api.Assertions.assertThat;

import com.aidevops.gateway.domain.DeviceId;
import com.aidevops.gateway.domain.SensorReading;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class SensorReadingMapperTests {

    @Test
    void mapsDomainToDocumentAndBack() {
        SensorReading reading = new SensorReading(
                new DeviceId("Cube1"),
                LocalDateTime.parse("2026-05-19T15:30:00"),
                24.5,
                61.2,
                832.5
        );

        SensorReadingDocument document = SensorReadingMapper.toDocument(reading);
        SensorReading restored = SensorReadingMapper.toDomain(document);

        assertThat(document.getDeviceId()).isEqualTo("Cube1");
        assertThat(document.getReceivedAt()).isNotNull();
        assertThat(restored).isEqualTo(reading);
    }
}
