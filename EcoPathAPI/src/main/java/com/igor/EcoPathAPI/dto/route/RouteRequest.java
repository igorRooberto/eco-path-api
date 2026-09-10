package com.igor.EcoPathAPI.dto.route;

import com.igor.EcoPathAPI.entites.enums.MobilityProfile;

public record RouteRequest(
        String originName,
        String destinationName,
        MobilityProfile profile,
        Coordinate originCoordinates,
        Coordinate destinationCoordinates
) {
}
