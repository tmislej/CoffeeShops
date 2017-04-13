package com.tine.coffeeshops.ui.main.map.manager;

import android.content.Context;
import android.location.Location;

import com.tine.coffeeshops.rx.location.LocationObservable;

import rx.Observable;

public class LocationManager {

    private final Context context;

    public LocationManager(Context context) {
        this.context = context;
    }

    public Observable<Location> requestLocationUpdates() {
        return Observable.create(new LocationObservable(context));
    }
}
