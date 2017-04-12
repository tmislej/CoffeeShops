package com.tine.coffeeshops;

import android.content.Context;
import android.content.res.Resources;

import com.tine.coffeeshops.api.service.PlacesApiService;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, ApiModule.class})
public interface AppComponent {

    void inject(App app);

    Context context();

    Resources resources();

    PlacesApiService placesApiService();
}
