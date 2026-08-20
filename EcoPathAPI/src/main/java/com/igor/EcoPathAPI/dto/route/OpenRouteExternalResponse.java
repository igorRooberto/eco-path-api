package com.igor.EcoPathAPI.dto.route;

import java.util.List;

public record OpenRouteExternalResponse(List<Feature> features) {
    public record Feature(Properties properties, Geometry geometry) {}
    public record Properties(Summary summary) {}
    public record Summary(Double distance, Double duration) {}
    public record Geometry(List<List<Double>> coordinates) {}
}
