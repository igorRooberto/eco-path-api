package com.igor.EcoPathAPI.repository;

import com.igor.EcoPathAPI.domain.model.RouteMetrics;

import java.util.List;
import java.util.Optional;

public interface RouteCacheRepository {

    void save(RouteMetrics routeMetrics);

    void saveAll(List<RouteMetrics> routeMetricsList);

    Optional<RouteMetrics> findById(String id);
}
