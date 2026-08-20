package com.igor.EcoPathAPI.client.weather;

import com.igor.EcoPathAPI.dto.Coordinate;
import com.igor.EcoPathAPI.dto.weather.WeatherMetrics;

import java.util.List;

public interface WeatherClient {

    List<WeatherMetrics> getCurrentWeather(List<Coordinate> coordinateList);
}
