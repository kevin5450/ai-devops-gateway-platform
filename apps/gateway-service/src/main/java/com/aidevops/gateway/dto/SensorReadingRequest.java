package com.aidevops.gateway.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record SensorReadingRequest(
        @NotBlank(message = "deviceId is required")
        String deviceId,

        @NotNull(message = "measuredAt is required")
        LocalDateTime measuredAt,

        @NotNull(message = "temperature is required")
        Double temperature,

        @NotNull(message = "humidity is required")
        @Min(value = 0, message = "humidity must be between 0 and 100")
        @Max(value = 100, message = "humidity must be between 0 and 100")
        Double humidity,

        @NotNull(message = "light is required")
        @DecimalMin(value = "0.0", message = "light must be greater than or equal to 0")
        Double light
) {
}
