package com.test.sixpro.ui.discovery.model;

import com.test.sixpro.bean.IndutyMustBean;
import com.test.sixpro.content.Urls;
import com.test.sixpro.retrofit.WRetrofit;
import com.test.sixpro.retrofit.impl.OnRetrofit;
import com.test.sixpro.utils.LogInfo;

import java.util.Map;

public class IContentModelImpl implements IContentModel {

    @Override
    public void loadContent(final ContentOnloadListener onloadListener) {

        WRetrofit.create().build(Urls.SERVER)
                .doGet(IndutyMustBean.class, Urls.URL_METHOD, new OnRetrofit.OnQueryMapListener<IndutyMustBean>() {
                    @Override
                    public void onMap(Map<String, String> map) {
                    }

                    @Override
                    public void onSuccess(IndutyMustBean beans) {
                        LogInfo.log("wwn", "IndutyMustBean---->" + beans.toString());
                        onloadListener.onComplete(beans);
                    }

                    @Override
                    public void onError(Throwable e) {
                        onloadListener.onError(e);
                    }
                });
    }
}
