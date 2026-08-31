package com.igor.EcoPathAPI.dto.route;

import com.igor.EcoPathAPI.dto.weather.AirQualityStatus;
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
            int distanceInMeters,
            int durationInSeconds
    ){}

    @Builder
    public record WeatherCheckPointDto(
            int order,
            double latitude,
            double longitude,
            double temperature,
            double windSpeed,
            int weatherCode,
            int airQualityIndex,
            AirQualityStatus airQualityStatus
    ){}

}
