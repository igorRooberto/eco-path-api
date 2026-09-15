package com.igor.EcoPathAPI.service;

import com.igor.EcoPathAPI.domain.model.RouteMetrics;
import com.igor.EcoPathAPI.domain.port.RoutingClient;
import com.igor.EcoPathAPI.dto.route.Coordinate;
import com.igor.EcoPathAPI.dto.route.RouteRequest;
import com.igor.EcoPathAPI.dto.route.RouteResponseDto;
import com.igor.EcoPathAPI.entites.enums.MobilityProfile;
import com.igor.EcoPathAPI.exception.base.IntegrationException;
import com.igor.EcoPathAPI.exception.base.NotFoundException;
import com.igor.EcoPathAPI.infrastructure.openRoute.dto.OpenRouteRequest;
import com.igor.EcoPathAPI.repository.RouteCacheRepository;
import com.igor.EcoPathAPI.strategy.CyclingRouteStrategy;
import com.igor.EcoPathAPI.strategy.RouteStrategy;
import com.igor.EcoPathAPI.strategy.RouteStrategyFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;


public class RouteServiceTest {

    private RoutingClient routingClient;
    private RouteStrategyFactory routeStrategyFactory;
    private RouteCacheRepository routeCacheRepository;
    private RouteStrategy routeStrategy;
    private RouteService routeService;
    private RouteRequest routeRequest;

    @BeforeEach
    void setUp(){
        routingClient = Mockito.mock(RoutingClient.class);
        routeStrategyFactory = Mockito.mock(RouteStrategyFactory.class);
        routeCacheRepository = Mockito.mock(RouteCacheRepository.class);
        routeStrategy = Mockito.mock(CyclingRouteStrategy.class);
        routeService = new RouteService(routingClient, routeStrategyFactory, routeCacheRepository);

        routeRequest = new RouteRequest("Home",
                "Work",
                MobilityProfile.CYCLING_REGULAR,
                new Coordinate(-16.3267, -48.9534),
                new Coordinate(-16.3300, -48.9500)
                );
    }

    @Test
    void shouldSimulateRouteWhenDataIsValid(){
        OpenRouteRequest openRouteRequest = OpenRouteRequest.builder()
                .coordinates(List.of(
                        List.of(routeRequest.originCoordinates().longitude(), routeRequest.originCoordinates().latitude()),
                        List.of(routeRequest.destinationCoordinates().longitude(), routeRequest.destinationCoordinates().latitude())
                ))
                .alternativeRoutes(
                        new OpenRouteRequest.AlternativeRoutes(3))
                .geometry(true)
                .build();

        List<RouteMetrics> routeMetrics = List.of(new RouteMetrics("id-1", 1700, 2400, "gemotry"));

        when(routeStrategyFactory.getStrategy(routeRequest.profile())).thenReturn(routeStrategy);
        when(routeStrategy.buildRequest(routeRequest.originCoordinates(), routeRequest.destinationCoordinates())).thenReturn(openRouteRequest);
        when(routingClient.calculateRouteMetrics(routeRequest.profile(), openRouteRequest)).thenReturn(routeMetrics);

        RouteResponseDto responseDto = routeService.simulateRoute(routeRequest);

        assertEquals("Home", responseDto.originName());
        assertEquals("Work", responseDto.destinationName());
        assertEquals(1, responseDto.routesInfoDto().size());

        verify(routeCacheRepository, times(1)).saveAll(routeMetrics);
    }

    @Test
    void shouldThrowExceptionWhenOpenRouteServiceCommunicationFails(){
        OpenRouteRequest openRouteRequest = OpenRouteRequest.builder()
                .coordinates(List.of(
                        List.of(routeRequest.originCoordinates().longitude(), routeRequest.originCoordinates().latitude()),
                        List.of(routeRequest.destinationCoordinates().longitude(), routeRequest.destinationCoordinates().latitude())
                ))
                .alternativeRoutes(
                        new OpenRouteRequest.AlternativeRoutes(3))
                .geometry(true)
                .build();

        when(routeStrategyFactory.getStrategy(routeRequest.profile())).thenReturn(routeStrategy);
        when(routeStrategy.buildRequest(routeRequest.originCoordinates(), routeRequest.destinationCoordinates())).thenReturn(openRouteRequest);
        when(routingClient.calculateRouteMetrics(routeRequest.profile(), openRouteRequest)).thenThrow(new IntegrationException("Falha de comunicação com OpenRouteService"));

        IntegrationException exception = assertThrows(IntegrationException.class, () -> routeService.simulateRoute(routeRequest));

        assertEquals("Falha de comunicação com OpenRouteService", exception.getMessage());

        verify(routeCacheRepository, never()).saveAll(anyList());
    }

    @Test
    void shouldThrowExceptionWhenRouteNotFound(){
        OpenRouteRequest openRouteRequest = OpenRouteRequest.builder()
                .coordinates(List.of(
                        List.of(routeRequest.originCoordinates().longitude(), routeRequest.originCoordinates().latitude()),
                        List.of(routeRequest.destinationCoordinates().longitude(), routeRequest.destinationCoordinates().latitude())
                ))
                .alternativeRoutes(
                        new OpenRouteRequest.AlternativeRoutes(3))
                .geometry(true)
                .build();

        when(routeStrategyFactory.getStrategy(routeRequest.profile())).thenReturn(routeStrategy);
        when(routeStrategy.buildRequest(routeRequest.originCoordinates(), routeRequest.destinationCoordinates())).thenReturn(openRouteRequest);
        when(routingClient.calculateRouteMetrics(routeRequest.profile(), openRouteRequest)).thenReturn(List.of());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> routeService.simulateRoute(routeRequest));

        assertEquals("Nenhuma Rota Encontrada", exception.getMessage());
        verify(routeCacheRepository, never()).saveAll(anyList());
    }
}
