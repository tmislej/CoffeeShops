package com.tine.coffeeshops.ui.main.map;

import android.content.res.Resources;
import android.support.annotation.Nullable;

import com.google.maps.android.clustering.ClusterManager;
import com.tine.coffeeshops.R;
import com.tine.coffeeshops.api.service.PlacesApiService;
import com.tine.coffeeshops.rx.location.MainThreadManager;
import com.tine.coffeeshops.ui.main.map.manager.LocationManager;
import com.tine.coffeeshops.ui.main.map.model.UiPlace;
import com.tine.coffeeshops.ui.main.map.ui.PlaceParseUtil;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;

public class CoffeeShopsMapPresenter implements CoffeeShopsMapMvp.Presenter {

    private static final int RADIUS = 250;

    private final CoffeeShopsMapMvp.View view;
    private final PlacesApiService placesApiService;
    private final Resources resources;
    private final MainThreadManager mainThreadManager;
    private final LocationManager locationManager;

    private ClusterManager<UiPlace> clusterManager;

    private boolean isMapReady = false;
    private Subscription locationSubscription;

    public CoffeeShopsMapPresenter(CoffeeShopsMapMvp.View view, PlacesApiService placesApiService,
            Resources resources, MainThreadManager mainThreadManager, LocationManager locationManager) {
        this.view = view;
        this.placesApiService = placesApiService;
        this.resources = resources;
        this.mainThreadManager = mainThreadManager;
        this.locationManager = locationManager;
    }

    @Override public void onReady() {
        if (isMapReady) {
            view.setMyLocationEnabled(true);
        }

        view.showLoading();
        locationSubscription = locationManager.requestLocationUpdates()
                .flatMap(location -> placesApiService.getNearbyPlaces(location.getLatitude(),
                        location.getLongitude(), PlacesApiService.TYPE_CAFE, RADIUS)
                        .onErrorResumeNext(Observable.empty())
                )
                .flatMap(placesResponseWrapper ->
                        Observable.from(placesResponseWrapper.getResults())
                                .map(placeResponse -> PlaceParseUtil.parsePlace(placeResponse, resources))
                                .toList())
                .compose(mainThreadManager.observableToMainThread())
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

    @Override public void onMapReady(ClusterManager<UiPlace> clusterManager) {
        this.clusterManager = clusterManager;
        isMapReady = true;
        view.setMyLocationEnabled(true);
    }

    @Override public void onDetachedFromWindow() {
        if (locationSubscription != null && !locationSubscription.isUnsubscribed()) {
            locationSubscription.unsubscribe();
        }
    }
}
