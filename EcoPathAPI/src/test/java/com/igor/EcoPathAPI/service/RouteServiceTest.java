package com.igor.EcoPathAPI.service;

import com.igor.EcoPathAPI.client.route.RoutingClient;
import com.igor.EcoPathAPI.client.weather.WeatherClient;
import com.igor.EcoPathAPI.dto.Coordinate;
import com.igor.EcoPathAPI.dto.route.RouteMetrics;
import com.igor.EcoPathAPI.dto.route.RouteRequest;
import com.igor.EcoPathAPI.dto.route.RouteResponseDto;
import com.igor.EcoPathAPI.dto.weather.AirQualityStatus;
import com.igor.EcoPathAPI.dto.weather.WeatherMetrics;
import com.igor.EcoPathAPI.exception.base.IntegrationException;
import org.apache.tomcat.util.http.fileupload.InvalidFileNameException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

public class RouteServiceTest {

    private RoutingClient routingClient;
    private WeatherClient weatherClient;
    private RouteService routeService;

    @BeforeEach
    void setUp(){
        routingClient = Mockito.mock(RoutingClient.class);
        weatherClient =  Mockito.mock(WeatherClient.class);

        routeService = new RouteService(routingClient, weatherClient);
    }

    @Test
    void shouldSimulateRouteAndReturnIntegerMetricsWhenRequestIsValid() {
        Coordinate origin = new Coordinate(-16.3267, -48.9534);
        Coordinate destination = new Coordinate(-16.3300, -48.9500);
        RouteRequest request = new RouteRequest("Casa", "Trabalho", origin, destination);

        Coordinate midPoint1 = new Coordinate(-16.3275, -48.9520);
        Coordinate midPoint2 = new Coordinate(-16.3290, -48.9510);

        List<Coordinate> fakeCoordinates = List.of(origin, midPoint1, midPoint2, destination);
        RouteMetrics fakeMetrics = new RouteMetrics(964, 1200, fakeCoordinates);

        WeatherMetrics fakeWeatherMetrics = new WeatherMetrics(33.7, 2, 1, 20, AirQualityStatus.GOOD);
        List<WeatherMetrics> ListFakeWeatherMetrics = List.of(fakeWeatherMetrics, fakeWeatherMetrics, fakeWeatherMetrics, fakeWeatherMetrics);

        when(routingClient.calculateRouteMetrics(origin, destination)).thenReturn(fakeMetrics);
        when(weatherClient.getCurrentWeather(anyList())).thenReturn(ListFakeWeatherMetrics);

        RouteResponseDto response = routeService.simulateRoute(request);

        assertNotNull(response);
        assertFalse(response.weatherForecast().isEmpty());

        assertEquals("Casa", response.originName());
        assertEquals("Trabalho", response.destinationName());

        assertEquals(964, response.routeInfoDto().distanceInMeters());
        assertEquals(1200, response.routeInfoDto().durationInSeconds());
        assertEquals(2, response.weatherForecast().size());
    }

    @Test
    void shouldThrowExceptionWhenRoutingClientFails() {

        Coordinate origin = new Coordinate(-16.3267, -48.9534);
        Coordinate destination = new Coordinate(-16.3300, -48.9500);
        RouteRequest request = new RouteRequest("Casa", "Trabalho", origin, destination);

        when(routingClient.calculateRouteMetrics(origin, destination))
                .thenThrow( new IntegrationException("Falha de comunicação com OpenRouteService"));

        assertThrows(IntegrationException.class, () -> {
            routeService.simulateRoute(request);
        });

        verify(weatherClient, never()).getCurrentWeather(anyList());
    }

    @Test
    void shouldThrowExceptionWhenWeatherClientFails() {
        Coordinate origin = new Coordinate(-16.3267, -48.9534);
        Coordinate destination = new Coordinate(-16.3300, -48.9500);
        RouteRequest request = new RouteRequest("Casa", "Trabalho", origin, destination);

        List<Coordinate> fakeCoordinates = List.of(origin, destination);
        RouteMetrics fakeMetrics = new RouteMetrics(964, 1200, fakeCoordinates);

        when(routingClient.calculateRouteMetrics(origin, destination)).thenReturn(fakeMetrics);

        when(weatherClient.getCurrentWeather(anyList()))
                .thenThrow(new IntegrationException ("Falha de comunicação com Open-Meteo"));

        assertThrows(IntegrationException.class, () -> {
            routeService.simulateRoute(request);
        });
    }




}
