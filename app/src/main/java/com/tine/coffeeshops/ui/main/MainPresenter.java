package com.tine.coffeeshops.ui.main;

public class MainPresenter implements MainMvp.Presenter {

    private final MainMvp.View view;

    public MainPresenter(MainMvp.View view) {
        this.view = view;
    }
}
