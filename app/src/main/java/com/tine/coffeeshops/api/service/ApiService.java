package com.tine.coffeeshops.api.service;

import com.tine.coffeeshops.api.model.PlacesResponseWrapper;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Single;

public interface ApiService {

    @GET("nearbysearch/json") Single<PlacesResponseWrapper> getNearbyPlaces(@Query("key") String apiKey,
            @Query("location") String location,
            @Query("type") String type, @Query("radius") int radius);
}
