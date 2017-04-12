package com.tine.coffeeshops.api.model;

import android.support.annotation.Nullable;

public class PlaceResponse {

    private String name;

    private OpeningHoursResponse openingHours;

    private GeometryResponse geometry;

    public String getName() {
        return name;
    }

    @Nullable public OpeningHoursResponse getOpeningHours() {
        return openingHours;
    }

    public GeometryResponse getGeometry() {
        return geometry;
    }
}
