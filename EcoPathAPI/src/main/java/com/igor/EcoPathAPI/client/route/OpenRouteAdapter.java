package com.igor.EcoPathAPI.client.route;

import com.igor.EcoPathAPI.dto.Coordinate;
import com.igor.EcoPathAPI.dto.route.apiResponse.OpenRouteExternalResponse;
import com.igor.EcoPathAPI.dto.route.RouteMetrics;
import com.igor.EcoPathAPI.exception.base.IntegrationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import java.time.Duration;
import java.util.List;

@Component
public class OpenRouteAdapter implements RoutingClient {

    private final RestClient restClient;
    private final String apiToken;

    public OpenRouteAdapter(@Value("${spring.open.route.url}") String urlAccess,
                            @Value("${spring.open.route.token}") String token) {
        this.restClient = RestClient.builder()
                .baseUrl(urlAccess)
                .build();
        this.apiToken = token;
    }

    @Override
    public RouteMetrics calculateRouteMetrics(Coordinate originCoordinates, Coordinate destinationCoordinates) {

        String startParam = originCoordinates.longitude() +","+ originCoordinates.latitude();
        String endParam = destinationCoordinates.longitude() + "," + destinationCoordinates.latitude();

        OpenRouteExternalResponse externalResponse = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("api_key",apiToken)
                        .queryParam("start",startParam)
                        .queryParam("end",endParam)
                        .build()
                )
                .retrieve().body(OpenRouteExternalResponse.class);

        return mapToDomain(externalResponse);
    }

    private RouteMetrics mapToDomain(OpenRouteExternalResponse externalResponse){
        if(externalResponse == null || externalResponse.features() == null || externalResponse.features().isEmpty()){
            throw new IntegrationException("Falha de comunicação com OpenRouteService");
        }

        var firstFeature = externalResponse.features().getFirst();
        var summary = firstFeature.properties().summary();

        int distanceInMeters = (int) Math.round(summary.distance());
        int durationInSeconds = (int) Math.round(summary.duration());

        List<Coordinate> coordinates = firstFeature.geometry().coordinates().stream()
                .map(point -> new Coordinate(point.get(1), point.getFirst())).toList();

        return new RouteMetrics(distanceInMeters, durationInSeconds, coordinates);
    }
}
