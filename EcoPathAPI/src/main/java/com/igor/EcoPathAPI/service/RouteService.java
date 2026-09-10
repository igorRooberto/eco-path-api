package com.igor.EcoPathAPI.service;

import com.igor.EcoPathAPI.domain.port.RoutingClient;
import com.igor.EcoPathAPI.domain.model.RouteMetrics;
import com.igor.EcoPathAPI.dto.route.RouteRequest;
import com.igor.EcoPathAPI.dto.route.RouteResponseDto;
import com.igor.EcoPathAPI.infrastructure.openRoute.dto.OpenRouteRequest;
import com.igor.EcoPathAPI.repository.RouteCacheRepository;
import com.igor.EcoPathAPI.strategy.RouteStrategy;
import com.igor.EcoPathAPI.strategy.RouteStrategyFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RouteService {

    private final RoutingClient routingClient;
    private final RouteStrategyFactory routeStrategyFactory;
    private final RouteCacheRepository routeCacheRepository;

    public RouteResponseDto simulateRoute(RouteRequest routeRequest) {

        RouteStrategy strategy = routeStrategyFactory.getStrategy(routeRequest.profile());

        OpenRouteRequest openRouteRequest = strategy.buildRequest(routeRequest.originCoordinates(), routeRequest.destinationCoordinates());

        List<RouteMetrics> metrics = routingClient.calculateRouteMetrics(routeRequest.profile(),openRouteRequest);

        routeCacheRepository.saveAll(metrics);

        List<RouteResponseDto.RouteSummaryDto> routeSummaryDtos = metrics.stream().map(metric ->
                new RouteResponseDto.RouteSummaryDto(
                        metric.routeId(),
                        metric.distanceInMeters(),
                        metric.durationInSeconds(),
                        metric.geometry())).toList();

        return new RouteResponseDto(
                routeRequest.originName(),
                routeRequest.destinationName(),
                routeSummaryDtos
        );
    }
}
