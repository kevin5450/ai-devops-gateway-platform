package com.aidevops.gateway.dto;

import com.aidevops.gateway.domain.SensorIssue;
import java.time.LocalDateTime;

public record SensorIssueResponse(
        String deviceId,
        LocalDateTime measuredAt,
        SensorIssue temperature,
        SensorIssue humidity
) {
}
