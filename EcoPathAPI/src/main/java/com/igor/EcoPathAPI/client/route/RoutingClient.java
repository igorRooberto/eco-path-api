package com.igor.EcoPathAPI.client.route;

import com.igor.EcoPathAPI.dto.Coordinate;
import com.igor.EcoPathAPI.dto.route.RouteMetrics;

public interface RoutingClient {

    RouteMetrics calculateRouteMetrics(Coordinate originCoordinates, Coordinate destinationCoordinates);
}
