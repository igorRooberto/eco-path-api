package com.igor.EcoPathAPI.client;

import com.igor.EcoPathAPI.dto.OpenRouteExternalResponse;
import com.igor.EcoPathAPI.dto.RouteMetrics;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import java.time.Duration;

@Component
public class OpenRouteServiceAdapter implements RoutingClient{

    private final RestClient restClient;
    private final String apiToken;

    public OpenRouteServiceAdapter(@Value("${spring.open.route.url}") String urlAccess,
                                   @Value("${spring.open.route.token}") String token) {
        this.restClient = RestClient.builder()
                .baseUrl(urlAccess)
                .build();
        this.apiToken = token;
    }

    @Override
    public RouteMetrics calculateRouteMetrics(String originCoordinates, String destinationCoordinates) {

        OpenRouteExternalResponse externalResponse = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("api_key",apiToken)
                        .queryParam("start",originCoordinates)
                        .queryParam("end",destinationCoordinates)
                        .build()
                )
                .retrieve().body(OpenRouteExternalResponse.class);

        if(externalResponse == null || externalResponse.features() == null || externalResponse.features().isEmpty()){
            throw new RuntimeException();
        }

        var firstFeature = externalResponse.features().getFirst();
        if (firstFeature.properties() == null || firstFeature.properties().summary() == null) {
            throw new RuntimeException();
        }

        var summary = externalResponse.features().getFirst().properties().summary();

        int distanceInCentimeters = (int) (summary.distance() * 100);
        Duration estimatedDuration = Duration.ofSeconds(summary.duration().longValue());

        return new RouteMetrics(distanceInCentimeters, estimatedDuration);
    }
}
