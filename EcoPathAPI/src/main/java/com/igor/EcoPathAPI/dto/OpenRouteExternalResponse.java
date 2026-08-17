package com.igor.EcoPathAPI.dto;

import java.util.List;

public record OpenRouteExternalResponse(List<Feature> features) {
    public record Feature(Properties properties) {}
    public record Properties(Summary summary) {}
    public record Summary(Double distance, Double duration) {}
}
