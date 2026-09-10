package com.igor.EcoPathAPI.infrastructure.openMeteo.dto;

public record AirQualityExternalResponse(Current current) {

    public record Current (int european_aqi){}
}
