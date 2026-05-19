package com.aidevops.gateway.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChatRequest(
        @NotBlank(message = "prompt is required")
        @Size(max = 4000, message = "prompt must be 4000 characters or fewer")
        String prompt,

        String userId
) {
}
