package com.test.sixpro.ui.discovery.persenter;

import java.lang.ref.WeakReference;

public class BasePersenter<T> {
    WeakReference<T> iContentView;

    public void attachView(T view) {
        this.iContentView = new WeakReference<T>(view);
    }
    public void detachView(){
        iContentView.clear();
    }
}
