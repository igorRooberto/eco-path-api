package com.igor.EcoPathAPI.dto.route;

import com.igor.EcoPathAPI.entites.enums.AirQualityStatus;
import lombok.Builder;

import java.util.List;

@Builder
public record RouteResponseDto(

        String originName,
        String destinationName,
        List<RouteSummaryDto> routesInfoDto
) {

    public record RouteSummaryDto(
            String routeId,
            int distanceInMeters,
            int durationInSeconds,
            String geometry
    ){}


}
