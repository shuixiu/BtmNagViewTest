package com.test.sixpro.base;

import android.os.Bundle;

import com.test.sixpro.ui.discovery.persenter.BasePersenter;

public abstract class BaseIContentActivity<V, T extends BasePersenter<V>> extends BaseActivity {

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        persenter = createPersenter();
        persenter.attachView((V) this);
    }

    public T persenter;

    protected abstract T createPersenter();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        persenter.detachView();
    }
}
