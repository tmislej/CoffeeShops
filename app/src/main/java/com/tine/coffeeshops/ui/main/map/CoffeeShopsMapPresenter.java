package com.tine.coffeeshops.ui.main.map;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.Nullable;

import com.google.android.gms.maps.GoogleMap;
import com.google.maps.android.clustering.ClusterManager;
import com.tine.coffeeshops.R;
import com.tine.coffeeshops.api.model.OpeningHoursResponse;
import com.tine.coffeeshops.api.model.PlaceResponse;
import com.tine.coffeeshops.api.service.PlacesApiService;
import com.tine.coffeeshops.rx.location.LocationObservable;
import com.tine.coffeeshops.ui.main.map.model.UiPlace;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public class CoffeeShopsMapPresenter implements CoffeeShopsMapMvp.Presenter {

    private static final int RADIUS = 250;

    private final CoffeeShopsMapMvp.View view;
    private final Context context;
    private final PlacesApiService placesApiService;
    private final Resources resources;

    private ClusterManager<UiPlace> clusterManager;

    private boolean isMapReady = false;
    private Subscription locationSubscription;

    public CoffeeShopsMapPresenter(CoffeeShopsMapMvp.View view, Context context, PlacesApiService placesApiService,
            Resources resources) {
        this.view = view;
        this.context = context;
        this.placesApiService = placesApiService;
        this.resources = resources;
    }

    @Override public void onReady() {
        if (isMapReady) {
            view.setMyLocationEnabled(true);
        }

        view.showLoading();
        locationSubscription = Observable.create(new LocationObservable(context))
                .flatMapSingle(location -> placesApiService.getNearbyPlaces(location.getLatitude(),
                        location.getLongitude(), PlacesApiService.TYPE_CAFE, RADIUS))
                .flatMap(placesResponseWrapper ->
                        Observable.from(placesResponseWrapper.getResults())
                                .map(this::parsePlace)
                                .toList())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<UiPlace>>() {
                    @Override public void onCompleted() {
                    }

                    @Override public void onError(Throwable e) {
                        view.showErrorSnabckbar(R.string.error_data);
                        view.hideLoading();
                    }

                    @Override public void onNext(@Nullable List<UiPlace> places) {
                        if (isMapReady) {
                            if (places != null && places.size() > 0) {
                                clusterManager.clearItems();
                                clusterManager.addItems(places);
                                clusterManager.cluster();
                            } else {
                                view.showInfoSnackbar(R.string.no_data);
                            }
                        }

                        view.hideLoading();
                    }
                });
    }

    @Override public void onMapReady(GoogleMap map) {
        isMapReady = true;
        clusterManager = new ClusterManager<>(context, map);
        view.setMyLocationEnabled(true);
        map.setOnCameraIdleListener(clusterManager);
        map.setOnMarkerClickListener(clusterManager);
    }

    @Override public void onDetachedFromWindow() {
        if (locationSubscription != null && !locationSubscription.isUnsubscribed()) {
            locationSubscription.unsubscribe();
        }
    }

    private UiPlace parsePlace(PlaceResponse placeResponse) {
        double latitude = placeResponse.getGeometry().getLocation().getLat();
        double longitude = placeResponse.getGeometry().getLocation().getLng();
        String name = placeResponse.getName();

        OpeningHoursResponse openingHours = placeResponse.getOpeningHours();
        String info;
        if (openingHours != null) {
            info = resources.getString(openingHours.isOpenNow() ? R.string.place_opened : R.string.place_closed);
        } else {
            info = resources.getString(R.string.place_opening_hours_unknown);
        }

        return new UiPlace(latitude, longitude, name, info);
    }
}
