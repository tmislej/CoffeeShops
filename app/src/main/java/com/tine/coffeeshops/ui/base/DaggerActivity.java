package com.tine.coffeeshops.ui.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.tine.coffeeshops.App;
import com.tine.coffeeshops.AppComponent;

public abstract class DaggerActivity<T> extends AppCompatActivity {

    private T component;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        component = inject();
    }

    protected T getComponent() {
        return component;
    }

    protected abstract T inject();

    @NonNull protected AppComponent getAppComponent() {
        return App.get(this).getComponent();
    }
}
