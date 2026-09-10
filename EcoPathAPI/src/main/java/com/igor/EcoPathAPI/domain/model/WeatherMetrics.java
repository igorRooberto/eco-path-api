package com.igor.EcoPathAPI.domain.model;

import com.igor.EcoPathAPI.entites.enums.AirQualityStatus;

public record WeatherMetrics(
        double temperature,
        double windSpeed,
        int weatherCode,
        int aqi,
        AirQualityStatus airQualityStatus
) {
}
