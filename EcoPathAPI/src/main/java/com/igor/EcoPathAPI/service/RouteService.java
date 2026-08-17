package com.igor.EcoPathAPI.service;

import com.igor.EcoPathAPI.client.RoutingClient;
import com.igor.EcoPathAPI.dto.RouteMetrics;
import com.igor.EcoPathAPI.dto.RouteRequest;
import com.igor.EcoPathAPI.dto.RouteResponseDto;
import com.igor.EcoPathAPI.entites.Route;
import com.igor.EcoPathAPI.repository.RouteRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RouteService {

    private final RoutingClient routingClient;

    public RouteService(RoutingClient routingClient) {
        this.routingClient = routingClient;
    }

    public RouteResponseDto simulateRoute(RouteRequest routeRequest){
        RouteMetrics metrics = routingClient.calculateRouteMetrics(routeRequest.originCoordinates(),
                routeRequest.destinationCoordinates());

        Route newRoute = Route.builder()
                .originName(routeRequest.originName())
                .destinationName(routeRequest.destinationName())
                .estimatedTime(metrics.estimatedTime())
                .distanceInCentimeters(metrics.distanceInCentimeters())
                .build();

        return new RouteResponseDto(
                newRoute.getOriginName(),
                newRoute.getDestinationName(),
                newRoute.getEstimatedTime(),
                newRoute.getDistanceInCentimeters()
                );
    }

}
