package com.igor.EcoPathAPI.dto;

import java.time.Duration;

public record RouteMetrics(
        int distanceInCentimeters,
        Duration estimatedTime
) {
}
