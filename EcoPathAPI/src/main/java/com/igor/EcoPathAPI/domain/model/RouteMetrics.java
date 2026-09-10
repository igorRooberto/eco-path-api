package com.igor.EcoPathAPI.domain.model;

import com.igor.EcoPathAPI.dto.route.Coordinate;

import java.util.List;

public record RouteMetrics(
        String routeId,
        int distanceInMeters,
        int durationInSeconds,
        String geometry
) {
}
