package com.tine.coffeeshops.api.model;

public class PlaceResponse {

    private String name;

    private OpeningHoursResponse openingHours;

    private GeometryResponse geometry;

    public String getName() {
        return name;
    }

    public OpeningHoursResponse getOpeningHours() {
        return openingHours;
    }

    public GeometryResponse getGeometry() {
        return geometry;
    }
}
