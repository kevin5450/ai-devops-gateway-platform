package com.aidevops.gateway.controller;

import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.blankOrNullString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class GatewayApiTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void healthReturnsGatewayStatus() throws Exception {
        mockMvc.perform(get("/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"))
                .andExpect(jsonPath("$.service").value("gateway-service"))
                .andExpect(jsonPath("$.timestamp", not(blankOrNullString())));
    }

    @Test
    void apiHealthReturnsGatewayStatus() throws Exception {
        mockMvc.perform(get("/api/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"))
                .andExpect(jsonPath("$.service").value("gateway-service"))
                .andExpect(jsonPath("$.timestamp", not(blankOrNullString())));
    }

    @Test
    void chatReturnsPlaceholderResponse() throws Exception {
        mockMvc.perform(post("/api/chat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "prompt": "hello",
                                  "userId": "demo-user"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.requestId", not(blankOrNullString())))
                .andExpect(jsonPath("$.userId").value("demo-user"))
                .andExpect(jsonPath("$.provider").value("gateway-placeholder"))
                .andExpect(jsonPath("$.message", not(blankOrNullString())))
                .andExpect(jsonPath("$.latencyMs").isNumber());
    }

    @Test
    void chatRejectsBlankPrompt() throws Exception {
        mockMvc.perform(post("/api/chat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "prompt": "",
                                  "userId": "demo-user"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.path").value("/api/chat"))
                .andExpect(jsonPath("$.message", not(blankOrNullString())));
    }

    @Test
    void createReadingStoresLatestReading() throws Exception {
        mockMvc.perform(post("/api/readings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "deviceId": "Cube1",
                                  "measuredAt": "2026-05-19T15:30:00",
                                  "temperature": 24.5,
                                  "humidity": 61.2,
                                  "light": 832.5
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.deviceId").value("Cube1"))
                .andExpect(jsonPath("$.message").value("Sensor reading accepted"));

        mockMvc.perform(get("/api/devices/Cube1/latest"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.deviceId").value("Cube1"))
                .andExpect(jsonPath("$.measuredAt").value("2026-05-19T15:30:00"))
                .andExpect(jsonPath("$.temperature").value(24.5))
                .andExpect(jsonPath("$.humidity").value(61.2))
                .andExpect(jsonPath("$.light").value(832.5));
    }

    @Test
    void latestReadingReturnsNotFoundWhenDeviceHasNoData() throws Exception {
        mockMvc.perform(get("/api/devices/UnknownCube/latest"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("No sensor reading found for deviceId: UnknownCube"));
    }

    @Test
    void latestIssueReportsHighTemperature() throws Exception {
        mockMvc.perform(post("/api/readings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "deviceId": "Cube2",
                                  "measuredAt": "2026-05-19T15:35:00",
                                  "temperature": 30.2,
                                  "humidity": 55.0,
                                  "light": 700.0
                                }
                                """))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/devices/Cube2/issues/latest"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.deviceId").value("Cube2"))
                .andExpect(jsonPath("$.temperature.status").value("HIGH"))
                .andExpect(jsonPath("$.humidity.status").value("OK"));
    }

    @Test
    void createReadingRejectsInvalidHumidity() throws Exception {
        mockMvc.perform(post("/api/readings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "deviceId": "Cube3",
                                  "measuredAt": "2026-05-19T15:40:00",
                                  "temperature": 24.5,
                                  "humidity": 120,
                                  "light": 832.5
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.path").value("/api/readings"))
                .andExpect(jsonPath("$.message", not(blankOrNullString())));
    }

    @Test
    void createReadingRejectsMissingDeviceId() throws Exception {
        mockMvc.perform(post("/api/readings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "measuredAt": "2026-05-19T15:45:00",
                                  "temperature": 24.5,
                                  "humidity": 60.0,
                                  "light": 832.5
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.path").value("/api/readings"))
                .andExpect(jsonPath("$.message", not(blankOrNullString())));
    }

    @Test
    void createReadingRejectsNegativeLight() throws Exception {
        mockMvc.perform(post("/api/readings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "deviceId": "Cube4",
                                  "measuredAt": "2026-05-19T15:50:00",
                                  "temperature": 24.5,
                                  "humidity": 60.0,
                                  "light": -1.0
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.path").value("/api/readings"))
                .andExpect(jsonPath("$.message", not(blankOrNullString())));
    }

    @Test
    void latestIssueReportsLowHumidity() throws Exception {
        mockMvc.perform(post("/api/readings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "deviceId": "Cube5",
                                  "measuredAt": "2026-05-19T15:55:00",
                                  "temperature": 23.0,
                                  "humidity": 30.0,
                                  "light": 500.0
                                }
                                """))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/devices/Cube5/issues/latest"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.deviceId").value("Cube5"))
                .andExpect(jsonPath("$.temperature.status").value("OK"))
                .andExpect(jsonPath("$.humidity.status").value("LOW"));
    }
}
