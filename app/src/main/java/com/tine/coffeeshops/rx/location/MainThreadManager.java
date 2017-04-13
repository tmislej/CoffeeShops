package com.tine.coffeeshops.rx.location;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

@Singleton
public class MainThreadManager {

    public Observable.Transformer observableTransformer;

    @Inject
    public MainThreadManager() {
        observableTransformer = o -> ((Observable) o).observeOn(AndroidSchedulers.mainThread());
    }

    @SuppressWarnings("unchecked") public <T> Observable.Transformer<T, T> observableToMainThread() {
        return observableTransformer;
    }
}
