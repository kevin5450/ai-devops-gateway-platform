package com.aidevops.gateway.domain;

public record SensorIssue(
        IssueStatus status,
        String message
) {
}
