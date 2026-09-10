package com.igor.EcoPathAPI.infrastructure.openMeteo;

import com.igor.EcoPathAPI.domain.port.WeatherClient;
import com.igor.EcoPathAPI.dto.route.Coordinate;
import com.igor.EcoPathAPI.infrastructure.openMeteo.dto.AirQualityExternalResponse;
import com.igor.EcoPathAPI.entites.enums.AirQualityStatus;
import com.igor.EcoPathAPI.infrastructure.openMeteo.dto.OpenMeteoWeatherResponse;
import com.igor.EcoPathAPI.domain.model.WeatherMetrics;
import com.igor.EcoPathAPI.exception.base.IntegrationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OpenMeteoAdapter implements WeatherClient {

    private final RestClient restClientWeather;
    private final RestClient restClientAir;

    public OpenMeteoAdapter(@Value("${spring.open.meteo.url}") String urlOpenMeteo,
                            @Value("${spring.open.air.url}") String urlAirMeteo) {
        this.restClientWeather = RestClient.builder()
                .baseUrl(urlOpenMeteo)
                .build();
        this.restClientAir = RestClient.builder()
                .baseUrl(urlAirMeteo)
                .build();
    }

    @Override
    public List<WeatherMetrics> getCurrentWeather(List<Coordinate> coordinatesList) {

        String latitudes = getLatitudes(coordinatesList);
        String longitudes = getLongitudes(coordinatesList);

        OpenMeteoWeatherResponse[] externalResponse = restClientWeather.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("latitude", latitudes)
                        .queryParam("longitude", longitudes)
                        .queryParam("current_weather", true)
                        .build()
                ).retrieve()
                .body(OpenMeteoWeatherResponse[].class);

        AirQualityExternalResponse[] airQualityExternalResponses = restClientAir.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("latitude", latitudes)
                        .queryParam("longitude", longitudes)
                        .queryParam("current", "european_aqi")
                        .build()
                ).retrieve()
                .body(AirQualityExternalResponse[].class);

        return mapToDomain(externalResponse, airQualityExternalResponses);
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

    private List<WeatherMetrics> mapToDomain(OpenMeteoWeatherResponse[] externalResponse, AirQualityExternalResponse[] airQualityExternalResponses){
        if (externalResponse == null || externalResponse.length == 0) {
            throw new IntegrationException("A API do OpenMeteo não retornou dados climáticos para as coordenadas solicitadas.");
        }

        if (airQualityExternalResponses == null || airQualityExternalResponses.length == 0) {
            throw new IntegrationException("A API do OpenMeteo não retornou dados sobre a Qualidade do Ar para as coordenadas solicitadas.");
        }

        List<WeatherMetrics> metrics = new ArrayList<>();

        for (int i = 0; i < externalResponse.length; i++){
            OpenMeteoWeatherResponse currentOpenMeteo = externalResponse[i];
            AirQualityExternalResponse currentAirQuality = airQualityExternalResponses[i];

            AirQualityStatus status = AirQualityStatus.fromAqi(currentAirQuality.current().european_aqi());

            metrics.add(new WeatherMetrics(
                    currentOpenMeteo.current_weather().temperature(),
                    currentOpenMeteo.current_weather().windspeed(),
                    currentOpenMeteo.current_weather().weathercode(),
                    currentAirQuality.current().european_aqi(),
                    status
                    ));
        }

        return metrics;
    }
}
