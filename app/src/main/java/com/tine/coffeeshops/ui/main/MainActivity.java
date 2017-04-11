package com.tine.coffeeshops.ui.main;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import com.tine.coffeeshops.R;
import com.tine.coffeeshops.ui.base.DaggerActivity;
import com.tine.coffeeshops.ui.main.map.CoffeeShopsMapView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends DaggerActivity<MainComponent> implements MainMvp.View {

    public static final int REQUEST_LOCATION_PERMISSION = 1;

    @BindView(R.id.map_view) CoffeeShopsMapView mapView;

    @Inject MainMvp.Presenter presenter;

    private Dialog rationaleDialog;

    // region activity callbacks

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mapView.onCreate(savedInstanceState);

        presenter.onCreate();
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

        if (rationaleDialog != null && rationaleDialog.isShowing()) {
            rationaleDialog.dismiss();
        }
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


    @Override public void requestPermission(String permission) {
        ActivityCompat.requestPermissions(this, new String[]{permission}, REQUEST_LOCATION_PERMISSION);
    }

    @Override public boolean shouldShowRationale(String permission) {
        return ActivityCompat.shouldShowRequestPermissionRationale(this, permission);
    }

    @Override public void hideRationaleDialog() {
        rationaleDialog.dismiss();
    }

    @Override protected MainComponent inject() {
        MainComponent component = DaggerMainComponent.builder()
                .appComponent(getAppComponent())
                .mainModule(new MainModule(this))
                .build();

        component.inject(this);

        return component;
    }

    @Override public void showRationale() {
        rationaleDialog = new AlertDialog.Builder(this)
                .setTitle(R.string.rationale_title)
                .setCancelable(false)
                .setMessage(getString(R.string.rationale_text, getString(R.string.app_name),
                        getString(R.string.location_permission)))
                .setPositiveButton(R.string.next, (dialog, which) -> presenter.onRationaleNext())
                .create();

        rationaleDialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        presenter.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
