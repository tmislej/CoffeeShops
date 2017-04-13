package com.tine.coffeeshops.ui.main;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import com.tine.coffeeshops.R;

public class MainPresenter implements MainMvp.Presenter {

    private static final String LOCATION_PERMISSION = Manifest.permission.ACCESS_FINE_LOCATION;

    private final MainMvp.View view;
    private final Context context;

    public MainPresenter(MainMvp.View view, Context context) {
        this.view = view;
        this.context = context;
    }

    @Override public void onCreate() {
        if (checkAndRequestPermission(LOCATION_PERMISSION)) {
            onPermissionGranted();
        }
    }

    @Override public void onRationaleNext() {
        view.hideRationaleDialog();
        view.requestPermission(LOCATION_PERMISSION);
    }

    @Override public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MainActivity.REQUEST_LOCATION_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    onPermissionGranted();
                } else {
                    onPermissionDeclined();
                }
                break;
            default:
                throw new IllegalStateException("Unknown request code");
        }
    }

    private void onPermissionGranted() {
        view.onReady();
    }

    private void onPermissionDeclined() {
        view.showToast(R.string.no_permission);
    }

    private boolean checkAndRequestPermission(String permission) {
        if (hasPermission(permission)) {
            return true;
        } else {
            if (view.shouldShowRationale(permission)) {
                view.showRationale();
            } else {
                view.requestPermission(permission);
            }

            return false;
        }
    }

    private boolean hasPermission(@NonNull String permission) {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }
}
