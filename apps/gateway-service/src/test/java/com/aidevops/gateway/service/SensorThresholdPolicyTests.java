package com.aidevops.gateway.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.aidevops.gateway.domain.IssueStatus;
import org.junit.jupiter.api.Test;

class SensorThresholdPolicyTests {

    private final SensorThresholdPolicy policy = new SensorThresholdPolicy();

    @Test
    void temperatureIsOkWithinRange() {
        assertThat(policy.evaluateTemperature(24.5).status()).isEqualTo(IssueStatus.OK);
    }

    @Test
    void temperatureIsHighAboveMaximum() {
        assertThat(policy.evaluateTemperature(30.0).status()).isEqualTo(IssueStatus.HIGH);
    }

    @Test
    void humidityIsLowBelowMinimum() {
        assertThat(policy.evaluateHumidity(30.0).status()).isEqualTo(IssueStatus.LOW);
    }

    @Test
    void humidityIsHighAboveMaximum() {
        assertThat(policy.evaluateHumidity(80.0).status()).isEqualTo(IssueStatus.HIGH);
    }
}
