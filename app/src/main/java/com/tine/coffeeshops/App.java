package com.tine.coffeeshops;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

public class App extends Application {

    private AppComponent component;

    @Override public void onCreate() {
        super.onCreate();
        inject();
    }

    private void inject() {
        component = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();

        component.inject(this);
    }

    @NonNull public AppComponent getComponent() {
        return component;
    }

    public static App get(@NonNull Context context) {
        return (App) context.getApplicationContext();
    }
}
