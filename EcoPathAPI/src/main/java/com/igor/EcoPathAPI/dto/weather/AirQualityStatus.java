package com.igor.EcoPathAPI.dto.weather;

public enum AirQualityStatus {

    GOOD,
    MODERATE,
    POOR,
    HAZARDOUS;

    public static AirQualityStatus fromAqi(int aqi) {
        if (aqi <= 50) return GOOD;
        if (aqi <= 100) return MODERATE;
        if (aqi <= 150) return POOR;
        return HAZARDOUS;
    }
}
