package com.tine.coffeeshops.rx.location;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.tine.coffeeshops.rx.location.exceptions.GoogleClientConnectionFailedException;
import com.tine.coffeeshops.rx.location.exceptions.GoogleClientConnectionSuspendedException;
import com.tine.coffeeshops.rx.location.exceptions.LocationPermissionNotGrantedException;

import rx.Observable;
import rx.Subscriber;

public class LocationObservable implements Observable.OnSubscribe<Location> {

    private static final int UPDATE_INTERVAL_MS = 2000; // prefered update interval in ms
    private static final int FASTEST_INTERVAL_MS = 1000; // fastest update interval in ms
    private static final int SMALLEST_DISPLACEMENT_M = 50; // smallest displacement in meters

    private final Context context;

    public LocationObservable(Context context) {
        this.context = context;
    }

    @Override public void call(Subscriber<? super Location> subscriber) {
        GoogleApiClient googleApiClient = createGoogleApiClient(subscriber);
        try {
            googleApiClient.connect();
        } catch (Throwable error) {
            subscriber.onError(error);
        }
    }

    private GoogleApiClient createGoogleApiClient(Subscriber<? super Location> subscriber) {

        ApiClientConnectionCallbacks callbacks = new ApiClientConnectionCallbacks(subscriber);

        GoogleApiClient apiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(callbacks)
                .addOnConnectionFailedListener(callbacks)
                .build();

        callbacks.setGoogleApiClient(apiClient);

        return apiClient;
    }

    private void onGoogleApiClientReady(GoogleApiClient googleApiClient, Subscriber<? super Location> subscriber) {
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            subscriber.onError(new LocationPermissionNotGrantedException());
        } else {
            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setInterval(UPDATE_INTERVAL_MS);
            locationRequest.setFastestInterval(FASTEST_INTERVAL_MS);
            locationRequest.setSmallestDisplacement(SMALLEST_DISPLACEMENT_M);
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

            Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            subscriber.onNext(lastLocation);

            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest,
                    subscriber::onNext);
        }
    }

    private class ApiClientConnectionCallbacks implements GoogleApiClient.ConnectionCallbacks,
            GoogleApiClient.OnConnectionFailedListener {

        private final Subscriber<? super Location> subscriber;
        private GoogleApiClient googleApiClient;

        ApiClientConnectionCallbacks(Subscriber<? super Location> subscriber) {
            this.subscriber = subscriber;
        }

        @Override public void onConnected(@Nullable Bundle bundle) {
            onGoogleApiClientReady(googleApiClient, subscriber);
        }

        @Override public void onConnectionSuspended(int i) {
            subscriber.onError(new GoogleClientConnectionSuspendedException());
        }

        @Override public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
            subscriber.onError(new GoogleClientConnectionFailedException());
        }

        void setGoogleApiClient(GoogleApiClient googleApiClient) {
            this.googleApiClient = googleApiClient;
        }
    }
}
