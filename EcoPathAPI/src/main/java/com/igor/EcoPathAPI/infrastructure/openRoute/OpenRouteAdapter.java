package com.igor.EcoPathAPI.infrastructure.openRoute;

import com.igor.EcoPathAPI.domain.port.RoutingClient;
import com.igor.EcoPathAPI.dto.route.Coordinate;
import com.igor.EcoPathAPI.entites.enums.MobilityProfile;
import com.igor.EcoPathAPI.infrastructure.openRoute.dto.OpenRouteExternalResponse;
import com.igor.EcoPathAPI.domain.model.RouteMetrics;
import com.igor.EcoPathAPI.exception.base.IntegrationException;
import com.igor.EcoPathAPI.infrastructure.openRoute.dto.OpenRouteRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.UUID;

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
    public List<RouteMetrics> calculateRouteMetrics(MobilityProfile mobilityProfile,OpenRouteRequest openRouteRequest) {

        OpenRouteExternalResponse externalResponse = restClient.post()
                .uri("v2/directions/" + mobilityProfile.getOrsProfile())
                .header("Authorization", apiToken)
                .header("Content-Type", "application/json")
                .body(openRouteRequest)
                .retrieve()
                .body(OpenRouteExternalResponse.class);

        return mapToDomain(externalResponse);
    }

    private List<RouteMetrics> mapToDomain(OpenRouteExternalResponse externalResponse){
        if(externalResponse == null || externalResponse.routes() == null || externalResponse.routes().isEmpty()){
            throw new IntegrationException("Falha de comunicação com OpenRouteService");
        }

        return externalResponse.routes().stream().map(route -> {
            var summary = route.summary();

            String routeId = UUID.randomUUID().toString();
            int distanceInMeters = (int) Math.round(summary.distance());
            int durationInSeconds = (int) Math.round(summary.duration());
            String geometry = route.geometry();

            return new RouteMetrics(routeId, distanceInMeters, durationInSeconds, geometry);
        }).toList();
    }


}
