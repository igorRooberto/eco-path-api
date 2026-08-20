package com.igor.EcoPathAPI.dto.route;

import lombok.Builder;

import java.time.Duration;
import java.util.List;

@Builder
public record RouteResponseDto(

        String originName,
        String destinationName,
        RouteSummaryDto routeInfoDto,
        List<WeatherCheckPointDto> weatherForecast
) {

    public record RouteSummaryDto(
            int distance,
            Duration estimatedTime
    ){}

    public record WeatherCheckPointDto(
            double temperature,
            double windSpeed,
            double weatherCode){}

}
