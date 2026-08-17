package com.igor.EcoPathAPI.client;

import com.igor.EcoPathAPI.dto.RouteMetrics;

public interface RoutingClient {

    RouteMetrics calculateRouteMetrics(String originCoordinates, String destinationCoordinates);
}
