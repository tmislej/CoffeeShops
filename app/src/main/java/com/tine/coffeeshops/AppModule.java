package com.tine.coffeeshops;

import android.content.Context;
import android.content.res.Resources;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    private final App app;

    public AppModule(App app) {
        this.app = app;
    }

    @Provides @Singleton App provideApplication() {
        return app;
    }

    @Provides @Singleton Context provideApplicationContext() {
        return app;
    }

    @Provides @Singleton Resources provideResources(Context context) {
        return context.getResources();
    }
}
