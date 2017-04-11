package com.tine.coffeeshops.ui.main;

public interface MainMvp {

    interface View {

        boolean shouldShowRationale(String permission);

        void requestPermission(String permission);

        void showRationale();

        void hideRationaleDialog();
    }

    interface Presenter {

        void onCreate();

        void onRationaleNext();

        void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults);
    }
}
