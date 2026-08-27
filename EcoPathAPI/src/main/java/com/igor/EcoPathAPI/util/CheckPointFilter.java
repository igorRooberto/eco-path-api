package com.igor.EcoPathAPI.util;


import com.igor.EcoPathAPI.dto.Coordinate;
import java.util.ArrayList;
import java.util.List;

public final class CheckPointFilter {

    private static final double CHECKPOINT_INTERVAL_KM = 10.0;
    private static final double MIN_DESTINATION_GAP_KM = 0.05;
    private static final double EARTH_RADIUS_KM = 6371.0;

    private CheckPointFilter(){
        throw new UnsupportedOperationException("Classe utilitária não pode ser instanciada");
    }

    public static List<Coordinate> extractCheckpoints(List<Coordinate> fullRoute){
        if (fullRoute == null || fullRoute.isEmpty()) {
            return List.of();
        }

        if (fullRoute.size() <= 2) {
            return fullRoute;
        }

        List<Coordinate> checkPoints = new ArrayList<>();

        Coordinate lastAccepted = fullRoute.getFirst();
        checkPoints.add(lastAccepted);

        for (int i = 1; i < fullRoute.size() - 1; i++) {
            Coordinate current = fullRoute.get(i);

            double distance = calculateDistanceInKm(
                    lastAccepted.latitude(), lastAccepted.longitude(),
                    current.latitude(), current.longitude()
            );

            if(distance >= CHECKPOINT_INTERVAL_KM){
                checkPoints.add(current);
                lastAccepted = current;
            }
        }

        Coordinate destination = fullRoute.getLast();

        double distanceToLast = calculateDistanceInKm(
                lastAccepted.latitude(), lastAccepted.longitude(),
                destination.latitude(), destination.longitude()
        );

        if (distanceToLast > MIN_DESTINATION_GAP_KM) {
            checkPoints.add(destination);
        }

        return checkPoints;
    }

    private static double calculateDistanceInKm(double lat1, double lon1, double lat2, double lon2) {

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS_KM * c;
    }
}
