package com.backelite.instantappssample.presenters;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by jean-baptistevincey on 13/06/2017.
 */

public abstract class AbstractPresenter<V> {

    protected V view;

    private CompositeDisposable composite;

    public AbstractPresenter() {
        composite = new CompositeDisposable();
    }

    public void bind(V view) {
        this.view = view;
    }

    public void unbind() {
        this.view = null;
    }

    protected void call(Disposable subscriber) {
        composite.add(subscriber);
    }

}
