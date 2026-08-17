package com.igor.EcoPathAPI.dto;

public record RouteRequest(
        String originName,
        String destinationName,
        String originCoordinates,
        String destinationCoordinates

) {
}
