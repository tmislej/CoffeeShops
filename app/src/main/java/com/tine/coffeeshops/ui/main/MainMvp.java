package com.tine.coffeeshops.ui.main;

import android.support.annotation.StringRes;

public interface MainMvp {

    interface View {

        boolean shouldShowRationale(String permission);

        void requestPermission(String permission);

        void showRationale();

        void hideRationaleDialog();

        void onReady();

        void showToast(@StringRes int text);
    }

    interface Presenter {

        void onCreate();

        void onRationaleNext();

        void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults);
    }
}
