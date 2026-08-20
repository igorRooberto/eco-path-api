package com.igor.EcoPathAPI.dto.route;

import com.igor.EcoPathAPI.dto.Coordinate;

import java.time.Duration;
import java.util.List;

public record RouteMetrics(
        int distanceInCentimeters,
        Duration estimatedTime,
        List<Coordinate> coordinates
) {
}
