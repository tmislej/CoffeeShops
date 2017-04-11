package com.tine.coffeeshops.ui.main;

import android.content.Context;

import com.tine.coffeeshops.di.scopes.ActivityScope;

import dagger.Module;
import dagger.Provides;

@Module
public class MainModule {

    private final MainMvp.View view;

    public MainModule(MainMvp.View view) {
        this.view = view;
    }

    @ActivityScope @Provides MainMvp.View provideView() {
        return view;
    }

    @ActivityScope @Provides MainMvp.Presenter providePresenter(MainMvp.View view, Context context) {
        return new MainPresenter(view, context);
    }
}
