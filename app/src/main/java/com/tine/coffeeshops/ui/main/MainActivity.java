package com.tine.coffeeshops.ui.main;

import android.os.Bundle;

import com.tine.coffeeshops.R;
import com.tine.coffeeshops.ui.base.DaggerActivity;
import com.tine.coffeeshops.ui.main.map.CoffeeShopsMapView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends DaggerActivity<MainComponent> implements MainMvp.View {

    @BindView(R.id.map_view) CoffeeShopsMapView mapView;

    @Inject MainMvp.Presenter presenter;

    // region activity callbacks

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mapView.onCreate(savedInstanceState);
    }

    @Override protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    // endregion activity callbacks

    @Override protected MainComponent inject() {
        MainComponent component = DaggerMainComponent.builder()
                .appComponent(getAppComponent())
                .mainModule(new MainModule(this))
                .build();

        component.inject(this);

        return component;
    }
}
