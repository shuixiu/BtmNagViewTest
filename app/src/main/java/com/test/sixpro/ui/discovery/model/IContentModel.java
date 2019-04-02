package com.test.sixpro.ui.discovery.model;

import com.test.sixpro.bean.IndutyMustBean;

import java.util.List;

public interface IContentModel {

    void loadContent(ContentOnloadListener onloadListener);

    interface ContentOnloadListener{

        //成功
        void onComplete(IndutyMustBean beans);
        //失败

        void onError(Throwable e);

    }

}
