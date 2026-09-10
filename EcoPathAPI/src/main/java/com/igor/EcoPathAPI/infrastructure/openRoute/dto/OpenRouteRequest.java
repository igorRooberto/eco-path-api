package com.igor.EcoPathAPI.infrastructure.openRoute.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.experimental.FieldNameConstants;

import java.util.List;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record OpenRouteRequest(
        List<List<Double>> coordinates,

        @JsonProperty("alternative_routes")
        AlternativeRoutes alternativeRoutes,

        boolean geometry

) {

    public record AlternativeRoutes(
            @JsonProperty("target_count")
            int targetCount ){}

    }


