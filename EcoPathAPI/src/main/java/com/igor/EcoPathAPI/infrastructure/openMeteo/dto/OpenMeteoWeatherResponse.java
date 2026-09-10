package com.igor.EcoPathAPI.infrastructure.openMeteo.dto;

public record OpenMeteoWeatherResponse(CurrentWeather current_weather) {

    public record CurrentWeather(
            double temperature,
            double windspeed,
            int weathercode
    ){

    }
}
