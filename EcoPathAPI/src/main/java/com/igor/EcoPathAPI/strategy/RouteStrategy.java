package com.igor.EcoPathAPI.strategy;

import com.igor.EcoPathAPI.dto.route.Coordinate;
import com.igor.EcoPathAPI.entites.enums.MobilityProfile;
import com.igor.EcoPathAPI.infrastructure.openRoute.dto.OpenRouteRequest;

import java.util.List;

public interface RouteStrategy {

    OpenRouteRequest buildRequest(Coordinate originCoordinate, Coordinate destinationCoordinate);
    MobilityProfile getProfile();
}
