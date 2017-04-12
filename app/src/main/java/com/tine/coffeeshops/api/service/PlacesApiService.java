package com.tine.coffeeshops.api.service;

import android.content.res.Resources;
import android.support.annotation.StringDef;

import com.tine.coffeeshops.R;
import com.tine.coffeeshops.api.model.PlacesResponseWrapper;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Locale;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.schedulers.Schedulers;

@Singleton
public class PlacesApiService {

    private final ApiService apiService;
    private final Resources resources;

    @Inject
    public PlacesApiService(ApiService apiService, Resources resources) {
        this.apiService = apiService;
        this.resources = resources;
    }

    public Observable<PlacesResponseWrapper> getNearbyPlaces(double latitude, double longitude, @PlaceType String type,
            int radius) {
        String apiKey = resources.getString(R.string.api_key);
        String location = String.format(Locale.US, "%f,%f", latitude, longitude);
        return apiService.getNearbyPlaces(apiKey, location, type, radius).subscribeOn(Schedulers.io());
    }

    // region StringDef

    @StringDef({TYPE_CAFE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface PlaceType {
    }

    public static final String TYPE_CAFE = "cafe";

    // endregion StringDef

}
