package com.igor.EcoPathAPI.service;

import com.igor.EcoPathAPI.client.route.RoutingClient;
import com.igor.EcoPathAPI.client.weather.WeatherClient;
import com.igor.EcoPathAPI.dto.Coordinate;
import com.igor.EcoPathAPI.dto.route.RouteMetrics;
import com.igor.EcoPathAPI.dto.route.RouteRequest;
import com.igor.EcoPathAPI.dto.route.RouteResponseDto;
import com.igor.EcoPathAPI.dto.weather.WeatherMetrics;
import com.igor.EcoPathAPI.entites.Route;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RouteService {

    private final RoutingClient routingClient;
    private final WeatherClient weatherClient;

    public RouteResponseDto simulateRoute(RouteRequest routeRequest){

        RouteMetrics metrics = routingClient.calculateRouteMetrics(routeRequest.originCoordinates(), routeRequest.destinationCoordinates());

        List<Coordinate> waypoints = extractCheckpoints(metrics.coordinates());
        List<WeatherMetrics> weatherMetrics = weatherClient.getCurrentWeather(waypoints);

        List<RouteResponseDto.WeatherCheckPointDto> weatherForecast = weatherMetrics.stream()
                .map(m -> new RouteResponseDto.WeatherCheckPointDto(m.temperature(), m.windSpeed(), m.weatherCode()))
                .toList();

        return RouteResponseDto.builder()
                .originName(routeRequest.originName())
                .destinationName(routeRequest.destinationName())
                .routeInfoDto(new RouteResponseDto.RouteSummaryDto(metrics.distanceInCentimeters(), metrics.estimatedTime()))
                .weatherForecast(weatherForecast)
                .build();
    }

    private List<Coordinate> extractCheckpoints(List<Coordinate> fullRoute) {
        if (fullRoute == null || fullRoute.isEmpty()) {
            return List.of();
        }

        if (fullRoute.size() <= 2) {
            return fullRoute;
        }

        Coordinate origin = fullRoute.getFirst();
        Coordinate destination = fullRoute.getLast();

        int middleIndex = fullRoute.size() / 2;
        Coordinate middle = fullRoute.get(middleIndex);

        return List.of(origin, middle, destination);
    }

}
