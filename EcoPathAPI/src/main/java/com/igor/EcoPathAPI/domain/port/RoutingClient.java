package com.igor.EcoPathAPI.domain.port;

import com.igor.EcoPathAPI.dto.route.Coordinate;
import com.igor.EcoPathAPI.domain.model.RouteMetrics;
import com.igor.EcoPathAPI.entites.enums.MobilityProfile;
import com.igor.EcoPathAPI.infrastructure.openRoute.dto.OpenRouteRequest;

import java.util.List;

public interface RoutingClient {

    List<RouteMetrics> calculateRouteMetrics(MobilityProfile mobilityProfile,OpenRouteRequest openRouteRequest);
}
