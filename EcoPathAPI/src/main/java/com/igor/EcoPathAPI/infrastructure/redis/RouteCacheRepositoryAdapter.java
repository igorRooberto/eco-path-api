package com.igor.EcoPathAPI.infrastructure.redis;

import com.igor.EcoPathAPI.domain.model.RouteMetrics;
import com.igor.EcoPathAPI.repository.RouteCacheRepository;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

@Component
public class RouteCacheRepositoryAdapter implements RouteCacheRepository {

    private static final String PREFIX = "route:";
    private static final long TTL_MINUTES = 30;

    private final StringRedisTemplate stringRedisTemplate;
    private final ObjectMapper objectMapper;

    public RouteCacheRepositoryAdapter(StringRedisTemplate stringRedisTemplate, ObjectMapper objectMapper) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public void save(RouteMetrics routeMetrics) {
        String json = objectMapper.writeValueAsString(routeMetrics);

        stringRedisTemplate.opsForValue().set(
                PREFIX + routeMetrics.routeId(),
                json,
                Duration.ofMinutes(TTL_MINUTES)
        );
    }

    @Override
    public void saveAll(List<RouteMetrics> routeMetricsList) {
        routeMetricsList.forEach(this::save);
    }

    @Override
    public Optional<RouteMetrics> findById(String id) {
        String json = stringRedisTemplate.opsForValue().get(PREFIX + id);

        if (json == null) {
            return Optional.empty();
        }

        RouteMetrics routeMetrics = objectMapper.readValue(json, RouteMetrics.class);
        return Optional.of(routeMetrics);
    }
}
