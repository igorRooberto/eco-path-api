package com.igor.EcoPathAPI.strategy;

import com.igor.EcoPathAPI.entites.enums.MobilityProfile;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
public class RouteStrategyFactory {

    private final Map<MobilityProfile, RouteStrategy> strategies =
            Map.of(MobilityProfile.CYCLING_REGULAR, new CyclingRouteStrategy(),
                   MobilityProfile.FOOT_WALKING,  new WalkingRouteStrategy()
            );

    public RouteStrategy getStrategy(MobilityProfile profile){
        return strategies.get(profile);
    }
}
