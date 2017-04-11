package com.tine.coffeeshops.ui.main;

import android.os.Bundle;

import com.tine.coffeeshops.R;
import com.tine.coffeeshops.ui.base.DaggerActivity;

import javax.inject.Inject;

public class MainActivity extends DaggerActivity<MainComponent> implements MainMvp.View {

    @Inject MainMvp.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override protected MainComponent inject() {
        MainComponent component = DaggerMainComponent.builder()
                .appComponent(getAppComponent())
                .mainModule(new MainModule(this))
                .build();

        component.inject(this);

        return component;
    }
}
