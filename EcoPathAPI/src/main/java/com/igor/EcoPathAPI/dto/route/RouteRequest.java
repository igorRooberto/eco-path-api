package com.igor.EcoPathAPI.dto.route;

import com.igor.EcoPathAPI.dto.Coordinate;

public record RouteRequest(
        String originName,
        String destinationName,
        Coordinate originCoordinates,
        Coordinate destinationCoordinates

) {
}
