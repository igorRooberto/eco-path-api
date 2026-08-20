package com.igor.EcoPathAPI.dto.weather;

public record WeatherMetrics(
        double temperature,
        double windSpeed,
        int weatherCode

) {
}
