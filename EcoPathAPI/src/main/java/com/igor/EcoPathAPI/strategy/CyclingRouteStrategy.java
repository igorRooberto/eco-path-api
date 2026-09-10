package com.igor.EcoPathAPI.strategy;

import com.igor.EcoPathAPI.dto.route.Coordinate;
import com.igor.EcoPathAPI.entites.enums.MobilityProfile;
import com.igor.EcoPathAPI.infrastructure.openRoute.dto.OpenRouteRequest;

import java.util.List;


public class CyclingRouteStrategy implements RouteStrategy{


    @Override
    public OpenRouteRequest buildRequest(Coordinate originCoordinate, Coordinate destinationCoordinate) {
        List<List<Double>> coordinates = List.of(List.of(originCoordinate.longitude(), originCoordinate.latitude()),
                                                 List.of(destinationCoordinate.longitude(), destinationCoordinate.latitude())
                );

        return OpenRouteRequest.builder()
                .coordinates(coordinates)
                .alternativeRoutes(new OpenRouteRequest.AlternativeRoutes(3))
                .geometry(true)
                .build();
    }

    @Override
    public MobilityProfile getProfile() {
        return MobilityProfile.CYCLING_REGULAR;
    }
}
