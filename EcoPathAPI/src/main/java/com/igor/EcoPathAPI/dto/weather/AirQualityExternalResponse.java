package com.igor.EcoPathAPI.dto.weather;

public record AirQualityExternalResponse(Current current) {

    public record Current (int european_aqi){}
}
