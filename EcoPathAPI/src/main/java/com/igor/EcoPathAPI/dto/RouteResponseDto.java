package com.igor.EcoPathAPI.dto;

import java.time.Duration;
import java.util.UUID;

public record RouteResponseDto(

        String originName,
        String destinationTime,
        Duration estimatedTime,
        int distance
) {
}
