package com.igor.EcoPathAPI.service;

import com.igor.EcoPathAPI.client.route.RoutingClient;
import com.igor.EcoPathAPI.client.weather.WeatherClient;
import com.igor.EcoPathAPI.dto.Coordinate;
import com.igor.EcoPathAPI.dto.route.RouteMetrics;
import com.igor.EcoPathAPI.dto.route.RouteRequest;
import com.igor.EcoPathAPI.dto.route.RouteResponseDto;
import com.igor.EcoPathAPI.dto.weather.WeatherMetrics;
import com.igor.EcoPathAPI.util.CheckPointFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RouteService {

    private final RoutingClient routingClient;
    private final WeatherClient weatherClient;

    public RouteResponseDto simulateRoute(RouteRequest routeRequest) {

        RouteMetrics metrics = routingClient.calculateRouteMetrics(routeRequest.originCoordinates(), routeRequest.destinationCoordinates());

        List<Coordinate> waypoints = CheckPointFilter.extractCheckpoints(metrics.coordinates());
        List<WeatherMetrics> weatherMetrics = weatherClient.getCurrentWeather(waypoints);

        List<RouteResponseDto.WeatherCheckPointDto> weatherForecast = buildWeatherForecast(waypoints, weatherMetrics);

        return RouteResponseDto.builder()
                .originName(routeRequest.originName())
                .destinationName(routeRequest.destinationName())
                .routeInfoDto(new RouteResponseDto.RouteSummaryDto(metrics.distanceInCentimeters(), metrics.estimatedTime()))
                .weatherForecast(weatherForecast)
                .build();
    }

    private List<RouteResponseDto.WeatherCheckPointDto> buildWeatherForecast(List<Coordinate> waypoints, List<WeatherMetrics> weatherMetrics) {

        List<RouteResponseDto.WeatherCheckPointDto> weatherForecast = new ArrayList<>();

        for (int i = 0; i < waypoints.size(); i++) {
            Coordinate coordinateCurrent = waypoints.get(i);
            WeatherMetrics weatherCurrent = weatherMetrics.get(i);

            weatherForecast.add(RouteResponseDto.WeatherCheckPointDto.builder()
                    .order(i + 1)
                    .latitude(coordinateCurrent.latitude())
                    .longitude(coordinateCurrent.longitude())
                    .temperature(weatherCurrent.temperature())
                    .windSpeed(weatherCurrent.windSpeed())
                    .weatherCode(weatherCurrent.weatherCode())
                    .airQualityIndex(weatherCurrent.aqi())
                    .airQualityStatus(weatherCurrent.airQualityStatus())
                    .build());
        }

        return weatherForecast;
    }




}
