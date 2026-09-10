package com.igor.EcoPathAPI.entites.enums;

public enum MobilityProfile {

    FOOT_WALKING("foot-walking"),
    CYCLING_REGULAR("cycling-regular");

    private final String orsProfile;

    MobilityProfile(String orsProfile) {
        this.orsProfile = orsProfile;
    }

    public String getOrsProfile() {
        return orsProfile;
    }

}
