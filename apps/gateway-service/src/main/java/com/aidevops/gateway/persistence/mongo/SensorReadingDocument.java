package com.aidevops.gateway.persistence.mongo;

import java.time.Instant;
import java.time.LocalDateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "sensor_readings")
@CompoundIndex(name = "device_measured_idx", def = "{'deviceId': 1, 'measuredAt': -1}")
public class SensorReadingDocument {

    @Id
    private String id;
    private String deviceId;
    private LocalDateTime measuredAt;
    private double temperature;
    private double humidity;
    private double light;
    private Instant receivedAt;

    public SensorReadingDocument() {
    }

    public SensorReadingDocument(
            String deviceId,
            LocalDateTime measuredAt,
            double temperature,
            double humidity,
            double light,
            Instant receivedAt
    ) {
        this.deviceId = deviceId;
        this.measuredAt = measuredAt;
        this.temperature = temperature;
        this.humidity = humidity;
        this.light = light;
        this.receivedAt = receivedAt;
    }

    public String getId() {
        return id;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public LocalDateTime getMeasuredAt() {
        return measuredAt;
    }

    public double getTemperature() {
        return temperature;
    }

    public double getHumidity() {
        return humidity;
    }

    public double getLight() {
        return light;
    }

    public Instant getReceivedAt() {
        return receivedAt;
    }
}
