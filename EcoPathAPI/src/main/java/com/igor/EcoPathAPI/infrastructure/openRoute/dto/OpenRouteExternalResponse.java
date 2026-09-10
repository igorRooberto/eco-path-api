package com.igor.EcoPathAPI.infrastructure.openRoute.dto;

import java.util.List;

public record OpenRouteExternalResponse(List<Route> routes) {

    public record Route(
            Summary summary,
            String geometry
    ) {}

    public record Summary(
            Double distance,
            Double duration
    ) {}

}
