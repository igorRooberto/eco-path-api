package com.igor.EcoPathAPI.client.weather;

import com.igor.EcoPathAPI.dto.Coordinate;
import com.igor.EcoPathAPI.dto.weather.OpenMeteoExternalResponse;
import com.igor.EcoPathAPI.dto.weather.WeatherMetrics;
import com.igor.EcoPathAPI.exception.OpenMeteoIntegrationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OpenMeteoAdapter implements WeatherClient{

    private final RestClient restClient;

    public OpenMeteoAdapter(@Value("${spring.open.meteo.url}") String urlOpenMeteo) {
        this.restClient = RestClient.builder()
                .baseUrl(urlOpenMeteo)
                .build();
    }

    @Override
    public List<WeatherMetrics> getCurrentWeather(List<Coordinate> coordinatesList) {

        String latitudes = getLatitudes(coordinatesList);
        String longitudes = getLongitudes(coordinatesList);

        OpenMeteoExternalResponse[] externalResponse = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("latitude", latitudes)
                        .queryParam("longitude", longitudes)
                        .queryParam("current_weather", true)
                        .build()
                ).retrieve()
                .body(OpenMeteoExternalResponse[].class);

        return mapToDomain(externalResponse);
    }

    private String getLatitudes(List<Coordinate> coordinates){
        return coordinates.stream()
               .map(coord -> String.valueOf(coord.latitude()))
               .collect(Collectors.joining(","));
    }

    private String getLongitudes(List<Coordinate> coordinates){
        return coordinates.stream()
                .map(coord -> String.valueOf(coord.longitude()))
                .collect(Collectors.joining(","));
    }

    private List<WeatherMetrics> mapToDomain(OpenMeteoExternalResponse[] externalResponse){
        if (externalResponse == null || externalResponse.length == 0) {
            throw new OpenMeteoIntegrationException("A API do OpenMeteo não retornou dados climáticos para as coordenadas solicitadas.");
        }

        return Arrays.stream(externalResponse)
                .map(response -> new WeatherMetrics(
                        response.current_weather().temperature(),
                        response.current_weather().windspeed(),
                        response.current_weather().weathercode()
                        ))
                        .toList();
    }
}
