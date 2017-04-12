package com.tine.coffeeshops.ui.main.map.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class UiPlace implements ClusterItem {

    private final LatLng location;
    private final String name;
    private final String info;

    public UiPlace(double latitude, double longitude, String name, String info) {
        location = new LatLng(latitude, longitude);
        this.name = name;
        this.info = info;
    }

    @Override public LatLng getPosition() {
        return location;
    }

    @Override public String getTitle() {
        return name;
    }

    @Override public String getSnippet() {
        return info;
    }
}
