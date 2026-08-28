package com.igor.EcoPathAPI.dto.weather;

public record OpenMeteoWeatherlResponse(CurrentWeather current_weather) {

    public record CurrentWeather(
            double temperature,
            double windspeed,
            int weathercode
    ){

    }
}
