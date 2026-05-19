package com.aidevops.gateway.service;

import com.aidevops.gateway.domain.IssueStatus;
import com.aidevops.gateway.domain.SensorIssue;
import org.springframework.stereotype.Component;

@Component
public class SensorThresholdPolicy {

    private static final double TEMP_MIN = 18.0;
    private static final double TEMP_MAX = 27.0;
    private static final double HUM_MIN = 40.0;
    private static final double HUM_MAX = 75.0;

    public SensorIssue evaluateTemperature(double temperature) {
        if (temperature < TEMP_MIN) {
            return new SensorIssue(IssueStatus.LOW, "Temperature is below safe range: below 18");
        }
        if (temperature > TEMP_MAX) {
            return new SensorIssue(IssueStatus.HIGH, "Temperature is above safe range: above 27");
        }
        return new SensorIssue(IssueStatus.OK, "No current issue");
    }

    public SensorIssue evaluateHumidity(double humidity) {
        if (humidity < HUM_MIN) {
            return new SensorIssue(IssueStatus.LOW, "Humidity is below safe range: below 40");
        }
        if (humidity > HUM_MAX) {
            return new SensorIssue(IssueStatus.HIGH, "Humidity is above safe range: above 75");
        }
        return new SensorIssue(IssueStatus.OK, "No current issue");
    }
}
