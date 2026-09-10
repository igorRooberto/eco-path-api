package com.igor.EcoPathAPI.domain.port;

import com.igor.EcoPathAPI.dto.route.Coordinate;
import com.igor.EcoPathAPI.domain.model.WeatherMetrics;

import java.util.List;

public interface WeatherClient {

    List<WeatherMetrics> getCurrentWeather(List<Coordinate> coordinateList);
}
